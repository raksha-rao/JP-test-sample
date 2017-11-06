package com.jptest.payments.fulfillment.testonia.business.amqdaemon;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import com.jpinc.kernel.cal.CalMessageHelper;
import com.jpinc.kernel.cal.api.CalEvent;
import com.jpinc.kernel.cal.api.sync.CalEventFactory;
import com.jpinc.kernel.logger.LogLevel;
import com.jpinc.kernel.logger.Logger;
import com.jptest.infra.amq.subscriber.exception.ConfigurationException;
import com.jptest.infra.amq.subscriber.util.AMQException;
import com.jptest.infra.amq.subscriber.util.AMQUtil;
import com.jptest.infra.util.CDBReader;
import com.jptest.infra.util.exception.ExceptionUtils;
import com.jptest.vo.serialization.Formats;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Testonia version of {@link com.jptest.infra.amq.subscriber.util.QueueCDBImpl}
 * that has extra getter setters near the end of file.
 * <p>
 * There are no other changes to this copied code.
 */
@SuppressWarnings("PMD")
public class QueueCDBImpl {
    private static final Logger LOGGER = Logger.getInstance(QueueCDBImpl.class);
    private static Map<String, List<String>> msgTypesInQs;
    private static Map<String, QueueCDBImpl> QProps;

    static {
        msgTypesInQs = new HashMap<>();
        QProps = new HashMap<>();
    }

    public static final int MAX_CDB_KEY_NUM_PARTITIONS = 512;
    public static final int MAX_CDB_KEY_MAX_SUBQUEUES_NUMBER = 2048;
    public static final String AMQ_MAP_FILE = "amqmap_amq.cdb";

    private Properties m_config = null;
    private Properties m_local_config = null;
    private List<String> m_message_types = null;
    private String m_namespace;
    private String m_queue_name;
    private List<Long> m_seq_num_list = new ArrayList<Long>();
    private Map<String, Long> messageTypeSeqNumMap = new HashMap<>();
    private String m_queue_occ_name;
    private List<String> m_queue_occ_list = new ArrayList<String>();;
    private long m_reconfig_time;
    private long[] m_partition_count = new long[2];
    private long[] m_partition_span = new long[2];
    private long[] m_subqueue_max = new long[2];
    private long m_max_dequeue_delay = 0;
    private long m_effective_dequeue_time_span = 0;

    private long m_large_payload_size_warning;

    // Others
    //-------------------------------
    private String m_require_amq_cdb;
    private int m_reconfig_mask;
    private String m_admin_occ_name;
    private boolean m_auto_seed_job;
    private Map<String, Formats> serializationFormatMap = new HashMap<>();

    // Log cdb parameters
    //-------------------------------
    private String m_log;

    // Useful to get q names for multiple queue subscription scenarios.

    public static String retrieveQNameForMsgType(String msgType) {
        String common_path = AMQUtil.get_amqcommon_path();
        String map_file_path = common_path + AMQ_MAP_FILE;

        CDBReader reader = null;
        try {
            reader = new CDBReader(new File(map_file_path));
        } catch (IOException ioe) {
            throw_config_ex(msgType, "Unable to read the CDB, " + map_file_path, null);
        }

        String key = String.format(AMQUtil.CDB_KEY_QUEUE_CDB, msgType);
        String file_name = reader.get(key);
        if (file_name == null) { // Not found for the message type in amqmap_amq.cdb, throws exception
            String msg = String.format("%s is missing in %s.", key, map_file_path);
            throw_config_ex(msgType, msg, null);
        }

        if (file_name.isEmpty()) { // Value is empty.
            String msg = String.format("%s: Value is empty.", key);
            throw_config_ex(msgType, msg, null);
        }

        //------------------------------------------
        // Load CDB for the message type.
        //------------------------------------------
        String cdb_file = common_path + file_name;

        try {
            reader = new CDBReader(new File(cdb_file));
        } catch (IOException ioe) {
            throw_config_ex(msgType, "Unable to read the CDB, " + cdb_file, null);
        }

        Properties p = new Properties();
        Set<String> keySet = (Set<String>) reader.keySet();
        for (String key1 : keySet) {
            p.put(key1, reader.get(key1));
        }

        // queue_name
        StringBuilder k = new StringBuilder();
        StringBuilder value = new StringBuilder();
        StringBuilder msg = new StringBuilder();
        k.setLength(0);
        value.setLength(0);

        k.append(String.format(AMQUtil.CDB_KEY_QUEUE_NAME, msgType));

        boolean fail = true;
        if (p.containsKey(k.toString())) {
            fail = false;
            value.append(p.getProperty(k.toString()));
        }

        if (fail) {
            msg.append(String.format(" %s parameter not found.", k.toString()));
            throw new ConfigurationException(msg.toString());
        }

        if (value.toString().isEmpty()) {
            msg.append(String.format(" [%s=] Value is empty.", k.toString()));
            throw new ConfigurationException(msg.toString());
        }
        String qName = value.toString();
        if (QueueCDBImpl.msgTypesInQs.get(qName) == null) {
            QueueCDBImpl.msgTypesInQs.put(qName, new ArrayList<String>());
        }
        msgTypesInQs.get(qName).add(msgType);

        return value.toString();
    }

    public static void setQProps(Properties config, String qName) {
        if (QueueCDBImpl.QProps.get(qName) == null) {
            QueueCDBImpl qProps = new QueueCDBImpl(config, getMessageTypesForQueue(qName));
            QueueCDBImpl.QProps.put(qName, qProps);
        }
    }

    public static QueueCDBImpl getQProps(String q) {
        return QueueCDBImpl.QProps.get(q);
    }

    public static List<String> getMessageTypesForQueue(String q) {
        if (QueueCDBImpl.msgTypesInQs.size() > 0) {
            return QueueCDBImpl.msgTypesInQs.get(q);
        } else {
            return null;
        }
    }

    public QueueCDBImpl(Properties config, List<String> message_types) {
        m_config = config;
        m_message_types = message_types;
        if (message_types.isEmpty()) {
            throw new AMQException("No message type specified.");
        }

        set_config(message_types.get(0));

        extract_cdb_values();
    }

    public long compute_partition_id(long time) {
        int i = get_entry_by_time(time);

        long pid = ((time / get_partition_span(i)) % get_partition_count(i)) + 1;

        return pid;
    }

    public long compute_partition_end(long time) {
        int i = get_entry_by_time(time);

        long end_time = (time / get_partition_span(i) + 1) * get_partition_span(i) - 1;

        return end_time;
    }

    public long compute_partition_span(long time) {
        int i = get_entry_by_time(time);

        return m_partition_span[i];
    }

    public long compute_subqueue_max(long time) {
        int i = get_entry_by_time(time);

        return m_subqueue_max[i];
    }

    public String get_queue_name() {
        return m_queue_name;
    }

    public String get_amq_namespace() {
        return m_namespace;
    }

    public String fix_message_type(String message_type) {
        if (m_namespace.isEmpty())
            return message_type;
        else
            return m_namespace + ":" + message_type;
    }

    public long get_seq_num(int i) {
        return m_seq_num_list.get(i);
    }

    public String get_queue_occ_name() {
        return m_queue_occ_name;
    }

    public Collection<String> get_queue_occ_list() {
        return m_queue_occ_list;
    }

    public long get_reconfig_time() {
        return m_reconfig_time;
    }

    public boolean is_reconfig_time_set() {
        return m_reconfig_time > 0;
    }

    public long get_partition_count(int i) {
        return m_partition_count[i];
    }

    public long get_partition_span(int i) {
        return m_partition_span[i];
    }

    public long get_min_partition_count() {
        if (m_partition_count[0] <= m_partition_count[1])
            return m_partition_count[0];
        else
            return m_partition_count[1];
    }

    public long get_min_partition_span() {
        if (m_partition_span[0] <= m_partition_span[1])
            return m_partition_span[0];
        else
            return m_partition_span[1];
    }

    public long get_subqueue_max(int i) {
        return m_subqueue_max[i];
    }

    public long get_max_dequeue_delay() {
        return m_max_dequeue_delay;
    }

    public long get_retain_time() {
        return m_effective_dequeue_time_span;
    }

    public String partition_count_to_string() {
        if (m_partition_count[0] != m_partition_count[1])
            return String.format("%d to %d", m_partition_count[0], m_partition_count[1]);
        else
            return String.format("%d", m_partition_count[0]);
    }

    public String partition_span_to_string() {
        if (m_partition_span[0] != m_partition_span[1])
            return String.format("%d to %d", m_partition_span[0] / 1000,
                    m_partition_span[1] / 1000);
        else
            return String.format("%d", m_partition_span[0] / 1000);
    }

    public String subqueue_max_to_string() {
        if (m_subqueue_max[0] != m_subqueue_max[1])
            return String.format("%d to %d", m_subqueue_max[0], m_subqueue_max[1]);
        else
            return String.format("%d", m_subqueue_max[0]);
    }

    public boolean is_subqueue_max_reconfig() {
        return (m_subqueue_max[0] < m_subqueue_max[1]);
    }

    public boolean is_require_amq_cdb_set() {
        return m_require_amq_cdb != null && !m_require_amq_cdb.isEmpty();
    }

    public List<String> getMessageTypes() {
        return m_message_types;
    }

    public boolean check_timeslot_size(long value, StringBuilder msg) {
        for (int i = 0; i < 1; i++) {
            if (m_partition_span[i] <= value ||
                    (m_partition_span[i] % value) != 0) {
                msg.append(
                        String.format("%s_partition_span=%s should be divisible and greater than %s_time_slot_size=%d",
                                m_queue_name,
                                partition_span_to_string(),
                                m_queue_name,
                                value / 1000));
                return false;
            }
        }

        return true;
    }

    public String get_admin_occ_name() {
        return m_admin_occ_name;
    }

    public boolean is_auto_seed_job() {
        return m_auto_seed_job;
    }

    public String get_cdb_log() {
        String log = m_log;

        return log.replace(' ', '+');
    }

    // Get the corresponding entry based on dequeue time
    protected int get_entry_by_time(long time) {
        if (m_reconfig_time == 0)
            return 0;

        time = AMQUtil.convert_ts_sec2ms(time);
        if (time >= m_reconfig_time)
            return 1;

        return 0;
    }

    protected static void throw_config_ex(String msgType, String msg, Throwable cause) {
        final String CAL_SEVERITY_TRANS_ERROR = "2";
        LOGGER.log(LogLevel.ERROR, msg);
        CalEvent calEvent = CalEventFactory.create("AMQ_QUEUE_CDB");
        calEvent.setName(msgType);
        calEvent.addData("error", msg);
        String statusString = CAL_SEVERITY_TRANS_ERROR + ".QueueCDB." + CalMessageHelper.ZERO + ".BadCDBConfig";
        calEvent.setStatus(statusString);
        calEvent.completed();

        throw ExceptionUtils.wrap(msg, cause, ConfigurationException.class, false);
    }

    protected void throw_config_exception(String msg, Throwable cause) {
        final String CAL_SEVERITY_TRANS_ERROR = "2";
        LOGGER.log(LogLevel.ERROR, msg);
        CalEvent calEvent = CalEventFactory.create("AMQ_QUEUE_CDB");
        calEvent.setName(m_queue_name);
        calEvent.addData("error", msg);
        String statusString = CAL_SEVERITY_TRANS_ERROR + ".QueueCDB." + CalMessageHelper.ZERO + ".BadCDBConfig";
        calEvent.setStatus(statusString);
        calEvent.completed();

        throw ExceptionUtils.wrap(msg, cause, ConfigurationException.class, false);
    }

    //-----------------------------------------------------------------
    // Locate the queue cdb file from the central location if any.
    //-----------------------------------------------------------------
    protected void set_config(String message_type) {

        m_local_config = m_config;

        String common_path = AMQUtil.get_amqcommon_path();
        String map_file_path = common_path + AMQ_MAP_FILE;

        CDBReader reader = null;
        try {
            reader = new CDBReader(new File(map_file_path));
        } catch (IOException ioe) {
            throw_config_exception("Unable to read the CDB, " + map_file_path, null);
        }

        String key = String.format(AMQUtil.CDB_KEY_QUEUE_CDB, message_type);
        String file_name = reader.get(key);
        if (file_name == null) { // Not found for the message type in amqmap_amq.cdb, throws exception
            String msg = String.format("%s is missing in %s.", key, map_file_path);
            throw_config_exception(msg, null);
        }

        if (file_name.isEmpty()) { // Value is empty.
            String msg = String.format("%s: Value is empty.", key);
            throw_config_exception(msg, null);
            // No return
        }

        m_require_amq_cdb = file_name;

        //------------------------------------------
        // Load CDB for the message type.
        //------------------------------------------
        String cdb_file = common_path + file_name;
        //m_log.append_formatted("&cdb=%s", cdb_file.chars());

        try {
            reader = new CDBReader(new File(cdb_file));
        } catch (IOException ioe) {
            throw_config_exception("Unable to read the CDB, " + cdb_file, null);
        }

        m_config = new Properties();
        Set<String> keySet = (Set<String>) reader.keySet();
        for (String key1 : keySet) {
            m_config.put(key1, reader.get(key1));
        }
    }
    /*
    protected void readMessageTypes(String q)
    {
        // message types
        //List<String> message_types = new ArrayList<String>();
        m_message_types =  new ArrayList<>();
      if (m_config.containsKey(AMQUtil.MESSAGE_TYPES)) {
          String strMessageTypes = m_config.getProperty(AMQUtil.MESSAGE_TYPES).replace('\t', ' ');
          StringTokenizer st = new StringTokenizer(strMessageTypes);
          while (st.hasMoreTokens())
          {
              String messageType = st.nextToken().trim();
              if (!messageType.isEmpty())
              {
                  m_message_types.add(messageType);
              }
          }
      }
      
      if (q == null && m_message_types.isEmpty()) {
          throw new AMQException("configuration property: " + AMQUtil.MESSAGE_TYPES + " is missing.");
      }
    }
    */

    @SuppressFBWarnings("SF_SWITCH_NO_DEFAULT")
    protected void extract_cdb_values() {
        StringBuilder key = new StringBuilder(), value = new StringBuilder();
        StringBuilder msg = new StringBuilder();

        //------------------------------------------
        // queue_name
        //------------------------------------------
        if (extract_value(AMQUtil.CDB_KEY_QUEUE_NAME,
                m_message_types.get(0),
                key,
                value,
                msg, null) != 0) {
            throw new ConfigurationException(msg.toString());
        }

        m_queue_name = value.toString();

        //------------------------------------------
        // All message types.
        //------------------------------------------
        for (int i = 0; i < m_message_types.size(); i++) {
            // queue_name
            if (i > 0) {
                if (extract_value(AMQUtil.CDB_KEY_QUEUE_NAME,
                        m_message_types.get(i),
                        key,
                        value,
                        msg, null) != 0) {
                    throw new ConfigurationException(msg.toString());
                }

                if (!m_queue_name.equalsIgnoreCase(value.toString())) {
                    msg.append(String.format(" [%s=%s] Queue name should be %s.",
                            key.toString(),
                            value.toString(),
                            m_queue_name));
                    throw new ConfigurationException(msg.toString());
                }
            }

            // seq_num
            if (extract_value(AMQUtil.CDB_KEY_SEQ_NUM,
                    m_message_types.get(i),
                    key,
                    value,
                    msg, null) == 0) {
                try {
                    long num = Long.parseLong(value.toString());
                    m_seq_num_list.add(num);
                    messageTypeSeqNumMap.put(m_message_types.get(i), num);
                    checkrange(key.toString(), num, msg, 1, 100);
                } catch (NumberFormatException e) {
                    throw new ConfigurationException(msg.toString());
                }
            }

            Formats serializationFormatVal = getSerializationFormatByMessageType(m_message_types.get(i), msg);
            serializationFormatMap.put(m_message_types.get(i), serializationFormatVal);

        } // All message types

        //------------------------------------------
        // amq_namespace
        //------------------------------------------
        extract_local_value(AMQUtil.CDB_KEY_QUEUE_NAMESPACE,
                null,
                key,
                value,
                null);
        m_namespace = value.toString();

        // Get my dbr queue OCC
        StringBuilder my_queue_occ_key = new StringBuilder();
        int queue_occ_status = extract_local_value(AMQUtil.CDB_KEY_QUEUE_OCCHOST_NAME_DBR,
                m_message_types.get(0),
                my_queue_occ_key,
                value,
                null);
        m_queue_occ_name = value.toString();

        //------------------------------------------
        // queue_occ = occ1 occ2 occ3 ...  (allow replicated queues in multiple DBs)
        // for subscriber
        //------------------------------------------
        String str = "", kw = "";
        if (extract_value(AMQUtil.CDB_KEY_QUEUE_OCCHOST_NAME,
                m_message_types.get(0),
                key,
                value,
                msg, null) == 0) {
            str = value.toString().replace('\t', ' ');
            StringTokenizer st = new StringTokenizer(str);
            while (st.hasMoreTokens()) {
                String tmp = st.nextToken();
                kw = tmp.trim();
                if (!kw.isEmpty()) {
                    m_queue_occ_list.add(kw);
                }
            }

            // m_queue_occ_list serves both publishers and subscribers
            if (queue_occ_status != 0 || !m_queue_occ_list.contains(m_queue_occ_name)) {
                switch (queue_occ_status) {
                case 0:
                    msg.append(String.format(" [%s=%s] OCC name for subscriber not found.", my_queue_occ_key,
                            m_queue_occ_name));
                    break;
                case -1:
                    m_queue_occ_name = m_queue_occ_list.get(0);
                    break;
                case -2:
                    msg.append(String.format(" [%s=] Value is empty.", my_queue_occ_key));
                    break;
                }
            }
        }

        //-------------------------------------------
        // Either partitions_no or partition_count
        //-------------------------------------------
        StringBuilder key2 = new StringBuilder();
        int ret = extract_value_exclusive(AMQUtil.CDB_KEY_NUM_PARTITIONS,
                AMQUtil.CDB_KEY_PARTITION_COUNT,
                m_queue_name,
                key,
                key2,
                value,
                msg);

        switch (ret) {
        case 0: { // partitions_no
            try {
                long num = Long.parseLong(value.toString());
                // is an integer!
                m_partition_count[0] = m_partition_count[1] = num;
            } catch (NumberFormatException e) {
                // not an integer!
                msg.append(String.format(" [%s=%s] Bad char found.", key, value));
                break;
            }

            checkrange(key.toString(), m_partition_count[0], msg,
                    3, MAX_CDB_KEY_NUM_PARTITIONS);

            break;
        }
        case 1: { // partition_count
            int count = extract_pair(value.toString(), m_partition_count, 1);
            if (count < 0) {
                msg.append(String.format(" [%s=%s] Bad value.", key2, value));
            } else if (count == 2 && m_partition_count[0] == m_partition_count[1]) {
                msg.append(String.format(" [%s=%s] Old value and new value are identical.",
                        key2, value));
            } else {
                checkrange(key.toString(), m_partition_count[0], msg,
                        3, MAX_CDB_KEY_NUM_PARTITIONS);
                if (count == 2) {
                    checkrange(key2.toString(), m_partition_count[1], msg,
                            3, MAX_CDB_KEY_NUM_PARTITIONS);
                }
            }

            break;
        }
        }

        //------------------------------------------
        // partition_span
        //------------------------------------------
        if (extract_value(AMQUtil.CDB_KEY_PARTITION_SPAN,
                m_queue_name,
                key,
                value,
                msg, null) == 0) {
            int count = extract_pair(value.toString(),
                    m_partition_span,
                    1000);
            if (count < 0) { // Error
                msg.append(String.format(" [%s=%s] Bad value.", key, value));
            } else if (count == 2 && m_partition_span[0] == m_partition_span[1]) {
                msg.append(String.format(" [%s=%s] Old value and new value are identical.",
                        key, value));
            } else if (m_partition_span[0] <= 1 ||
                    m_partition_span[1] <= 1)
                msg.append(String.format(" [%s=%s] Value is less than 2.",
                        key, value));

            //------------------------------------------
            // Validate reconfig_time
            //------------------------------------------
            else if (extract_value(AMQUtil.CDB_KEY_RECONFIG_TIME,
                    m_queue_name,
                    key,
                    value,
                    null, null) == 0) {
                try {
                    m_reconfig_time = Long.parseLong(value.toString());
                } catch (NumberFormatException e) {
                    msg.append(String.format(" [%s=%s] Value must be valid number.",
                            key, value));
                }

                if (m_reconfig_time <= 0) {
                    msg.append(String.format(" [%s=%s] Value must be greater than 0.",
                            key, value));
                }

                int span = (int) (m_partition_span[0] / 1000);
                if ((m_reconfig_time % span) != 0) {
                    msg.append(String.format(" [%s=%s] Value must be divisible by %d.",
                            key, value, span));
                }

                m_reconfig_time *= 1000;
                m_reconfig_mask |= 0x1;
            }
        }

        //------------------------------------------
        // Either subqueues_no or subqueue_max
        //------------------------------------------
        ret = extract_value_exclusive(AMQUtil.CDB_KEY_MAX_SUBQUEUES_NUMBER,
                AMQUtil.CDB_KEY_SUBQUEUE_MAX,
                m_queue_name,
                key,
                key2,
                value,
                msg);

        switch (ret) {
        case 0: { // subqueues_no
            try {
                m_subqueue_max[0] = m_subqueue_max[1] = Long.parseLong(value.toString());
            } catch (NumberFormatException e) {
                msg.append(String.format(" [%s=%s] Bad char found.", key, value));
                break;
            }

            checkrange(key.toString(), m_subqueue_max[0], msg,
                    1, MAX_CDB_KEY_MAX_SUBQUEUES_NUMBER);
            break;
        }
        case 1: { // subqueue_max
            String config_key = "";
            config_key += String.format(AMQUtil.CDB_KEY_SUBQUEUES_RANGES_NO,
                    m_queue_name);
            int count = extract_pair(value.toString(),
                    m_subqueue_max,
                    1);
            if (count < 0) {
                msg.append(String.format(" [%s=%s] Bad value.", key2, value));
            } else if (count == 2 && m_subqueue_max[0] >= m_subqueue_max[1]) {
                msg.append(String.format(" [%s=%s] New value must be greater than the old value.",
                        key2, value));
            } else if (count == 2 && ((m_config.containsKey(config_key)) || (m_local_config.containsKey(config_key)))) { // subqueues_ranges_no is also set.
                                                                                                                         // If subqueue_max has a pair of numbers, then subqueues_ranges_no cannot be set.
                msg.append(String.format(
                        " %s should be replaced with %s_subqueue_range_size when %s has a pair of values.",
                        config_key, m_queue_name, key2));
            } else {
                checkrange(key.toString(), m_subqueue_max[0], msg,
                        1, MAX_CDB_KEY_MAX_SUBQUEUES_NUMBER);
                if (count == 2) {
                    checkrange(key2.toString(), m_subqueue_max[1], msg,
                            1, MAX_CDB_KEY_MAX_SUBQUEUES_NUMBER);
                }
            }
            break;
        }
        }

        //------------------------------------------
        // max_dequeue_delay
        //------------------------------------------
        if (extract_value(AMQUtil.CDB_KEY_MAX_DEQUEUE_DELAY,
                m_queue_name,
                key,
                value,
                null, null) == 0) {
            try {
                long num = Long.parseLong(value.toString());
                m_max_dequeue_delay = 1000 * num;
                if (m_max_dequeue_delay < 0)
                    msg.append(String.format(" [%s=%s] Value is less than 0.",
                            key, value));
            } catch (NumberFormatException e) {
                msg.append(String.format(" [%s=%s] Bad char found.", key, value));
            }
        }

        //------------------------------------------
        // Verify reconfig_time setting
        //------------------------------------------
        key.setLength(0);
        key.append(String.format(AMQUtil.CDB_KEY_RECONFIG_TIME, m_queue_name));
        if ((m_reconfig_mask & 0x3) == 1) {
            msg.append(String.format(
                    " %s can be set only if partition_count, partition_span, or subqueue_max has a pair of values for reconfig.",
                    key));
        }

        if ((m_reconfig_mask & 0x3) == 2) {
            msg.append(String.format(
                    " %s must be set if partition_count, partition_span, or subqueue_max has a pair of values.", key));
        }

        //------------------------------------------
        // effective_dequeue_time_span
        //------------------------------------------
        if (extract_value(AMQUtil.CDB_KEY_EFFECTIVE_DEQ_SPAN,
                m_queue_name,
                key,
                value,
                null, null) == 0) {
            try {
                long num = Long.parseLong(value.toString());
                m_effective_dequeue_time_span = 1000 * num;
                if (m_effective_dequeue_time_span < 0)
                    msg.append(String.format(" [%s=%s] Value is less than 0.",
                            key, value));
            } catch (NumberFormatException e) {
                msg.append(String.format(" [%s=%s] Bad char found.", key, value));
            }
        }

        //------------------------------------------
        // amq_admin_occ
        //------------------------------------------
        if (extract_value(AMQUtil.CDB_KEY_ADMIN_OCCHOST_NAME,
                "",
                key,
                value,
                null, null) != 0) {
            m_admin_occ_name += AMQUtil.AMQ_ADMIN_DEFAULT_DB;
        } else {
            m_admin_occ_name = value.toString();
        }

        if (m_admin_occ_name.equals("null") ||
                m_local_config == m_config) {
            m_admin_occ_name = "";
        }

        // One-off for QA testing AMQ_ADMIN_ST on hyper.
        if (m_local_config.equals(m_config) && AMQUtil.is_switch_enabled(m_local_config, "amq_qa_testing", false)) {
            m_admin_occ_name = "CONF";
            m_require_amq_cdb = "qa_testing_dummy";
        }

        m_auto_seed_job = AMQUtil.is_switch_enabled(m_local_config, "amq_auto_seed_job", true);

        //m_enqueue_in_seconds = false;

        if (m_local_config.containsKey(AMQUtil.CDB_KEY_ENQUEUE_IN_MILLISECONDS))
            msg.append(String.format(" %s is no longer supported.",
                    AMQUtil.CDB_KEY_ENQUEUE_IN_MILLISECONDS));

        if (!m_local_config.containsKey(AMQUtil.CDB_KEY_LARGE_PAYLOAD_SIZE_WARNING)) {
            m_large_payload_size_warning = -1;
        } else {
            String paysize = m_local_config.getProperty(AMQUtil.CDB_KEY_LARGE_PAYLOAD_SIZE_WARNING);
            try {
                long num = Long.parseLong(paysize);
                m_large_payload_size_warning = num;
            } catch (NumberFormatException e) {
                msg.append(
                        String.format(" [%s=%s] Bad char found.", AMQUtil.CDB_KEY_LARGE_PAYLOAD_SIZE_WARNING, paysize));
            }
        }

        if (!msg.toString().isEmpty()) {
            String exc_msg = "***[ERROR] Bad configuration: " + msg.toString();

            throw_config_exception(exc_msg, null);
            // No return;
        }

        // Succeeded
    }

    /**
     *Extract a value string for a parameter.
     * return:  0  ok
     * -1  not found
     * -2  empty value
     */
    int extract_value(String fmt,
            String prefix,
            StringBuilder key,
            StringBuilder value,
            StringBuilder msg,
            Properties myConfig) {
        key.setLength(0);
        value.setLength(0);

        if (prefix == null)
            key.append(fmt);
        else
            key.append(String.format(fmt, prefix));

        boolean fail = true;
        if (myConfig != null && !myConfig.isEmpty()) {
            if (myConfig.containsKey(key.toString())) {
                fail = false;
                value.append(myConfig.getProperty(key.toString()));
            }
        } else {
            if (m_config.containsKey(key.toString())) {
                fail = false;
                value.append(m_config.getProperty(key.toString()));
            }
        }

        if (fail) {
            if (msg != null)
                msg.append(String.format(" %s parameter not found.", key.toString()));

            return -1;
        }

        if (value.toString().isEmpty()) {
            if (msg != null)
                msg.append(String.format(" [%s=] Value is empty.", key.toString()));

            m_log += String.format("&%s=", key.toString());
            return -2;
        }

        m_log += String.format("&%s=%s", key, value);
        return 0;
    }

    int extract_local_value(String fmt,
            String prefix,
            StringBuilder key,
            StringBuilder value,
            StringBuilder msg) {
        return extract_value(fmt, prefix, key, value, msg, m_local_config);
    }

    //-----------------------------------------------------------------
    // Extract a value from one of the two parameters exclusively.
    // return:  0 first key is used.
    //            1 second key is used.
    //           -1 error.
    //-----------------------------------------------------------------
    int extract_value_exclusive(String fmt,
            String fmt2,
            String prefix,
            StringBuilder key,
            StringBuilder key2,
            StringBuilder value,
            StringBuilder msg) {
        key.setLength(0);
        key2.setLength(0);
        value.setLength(0);
        StringBuilder value2 = new StringBuilder();
        StringBuilder local_msg = new StringBuilder();
        int error = extract_value(fmt, prefix, key, value, local_msg, null);
        int error2 = extract_value(fmt2, prefix, key2, value2, local_msg, null);

        if (error != 0 && error2 != 0) { // Neither parameter is set.
            msg.append(String.format(" Neither %s nor %s is set.", key, key2));
            return -1;
        } else if (error == 0 && error2 == 0) { // Both parameters are set.
            msg.append(String.format(" Both %s and %s are set.", key, key2));
            return -1;
        }

        if (error == 0)
            return 0;

        if (error2 == 0) {
            value.append(value2);
            return 1;
        }
        return -1;
    }

    //-------------------------------------------------------------------------------------------------------------------
    // Extract a pair of numbers from a '<a> to <b>' String. Returns -1 if fails, 1 if single value, 2 if pair of values
    //-------------------------------------------------------------------------------------------------------------------
    int extract_pair(String value,
            long[] a,
            //long b,
            int scale) {
        int count = 0;

        int off = value.indexOf(" to ");
        if (off == 0 || (off != -1 && off + 4 == value.length()))
            return -1;

        if (off == -1) {
            try {
                long num = Long.parseLong(value);
                // is an integer!
                a[0] = a[1] = num * scale;
                count = 1;
            } catch (NumberFormatException e) {
                // not an integer!
                return -1;
            }

        } else {
            try {
                long num1 = Long.parseLong(value.substring(0, off));
                long num2 = Long.parseLong(value.substring(off + 4, value.length()));
                a[0] = num1 * scale;
                a[1] = num2 * scale;
                count = 2;

                m_reconfig_mask |= 0x2;
            } catch (NumberFormatException e) {
                // not an integer!
                return -1;
            }
        }

        return count;
    }

    boolean checkrange(String config_key,
            long config_value,
            StringBuilder msg,
            int minval,
            int maxval) {
        if ((config_value < minval || config_value > maxval)) {
            msg.append(String.format(" %s=%d is out of range[%d,%d]",
                    config_key,
                    config_value,
                    minval,
                    maxval));
            return false; // failed
        }

        return true; // passed
    }

    public Map<String, Long> getMessageTypeSeqNumMap() {
        return messageTypeSeqNumMap;
    }

    public Map<String, Formats> getSerializationFormatMap() {
        return serializationFormatMap;
    }

    private Formats getSerializationFormatByMessageType(String messageType, StringBuilder errorMsg) {
        String serializationFormatKey = String.format(AMQUtil.CDB_KEY_SERIALIZATION_METHOD, messageType);
        Formats serializationFormatVal = Formats.BINARY;
        if (m_config.containsKey(serializationFormatKey)) {
            String val = m_config.getProperty(serializationFormatKey);
            if (val != null) {
                val = val.toUpperCase();
                boolean isProperSerializationFormat = false;
                for (Formats format : Formats.values()) {
                    if (format.toString().equals(val)) {
                        serializationFormatVal = format;
                        isProperSerializationFormat = true;
                        break;
                    }
                }
                if (!isProperSerializationFormat) {
                    String tempErrorMsg = "Invalid serialization format= " + val + ", for messageType=" + messageType;
                    tempErrorMsg = tempErrorMsg + " , defaulting it to " + serializationFormatVal;
                    LOGGER.log(LogLevel.WARN, tempErrorMsg);
                    CalEvent calEvent = CalEventFactory.create("AMQ_QUEUE_CDB_WARN", messageType, "0", tempErrorMsg);
                    calEvent.completed();
                    //                    errorMsg = errorMsg.append("Invalid serialization format= ").append(val).append(", for messageType= ").append(messageType);
                }
            }
        }
        return serializationFormatVal;
    }

    /*** START: TESTONIA RELATED CHANGES ***/
    public Properties getConfig() {
        return m_config;
    }

    public void setLocalConfig(Properties localConfig) {
        this.m_local_config = localConfig;
    }
    /*** END: TESTONIA RELATED CHANGES ***/

}
