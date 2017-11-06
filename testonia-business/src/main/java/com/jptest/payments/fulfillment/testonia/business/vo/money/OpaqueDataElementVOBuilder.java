package com.jptest.payments.fulfillment.testonia.business.vo.money;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.jptest.common.OpaqueDataElementVO;
import com.jptest.payments.fulfillment.testonia.business.vo.ListWrappedVOBuilder;
import com.jptest.vo.ValueObject;
import com.jptest.vo.serialization.Formats;
import com.jptest.vo.serialization.UniversalSerializer;

public class OpaqueDataElementVOBuilder extends ListWrappedVOBuilder<OpaqueDataElementVO> {

    private List<ValueObject> valueObjects = new ArrayList<>();
    private Byte serializationForm = Byte.valueOf((byte) 'B');

    public static OpaqueDataElementVOBuilder newBuilder() {
        return new OpaqueDataElementVOBuilder();
    }

    public OpaqueDataElementVOBuilder vo(ValueObject valueObject) {
        this.valueObjects.add(valueObject);
        return this;
    }

    @Override
    public List<OpaqueDataElementVO> buildList() {
        List<OpaqueDataElementVO> voList = new ArrayList<OpaqueDataElementVO>();
        if (CollectionUtils.isNotEmpty(valueObjects)) {
            for (ValueObject valueObject : valueObjects) {
                OpaqueDataElementVO vo = new OpaqueDataElementVO();
                vo.setClassName(valueObject.voClassName());
                vo.setSerializationForm(serializationForm);
                vo.setSerializedData(serializeValueObject(valueObject));
                voList.add(vo);
            }
        }
        return voList;
    }

    private static byte[] serializeValueObject(ValueObject vo) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            new UniversalSerializer(Formats.COMPRESSEDBINARY, false, true, false).serialize(vo, os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return os.toByteArray();
    }

    @Override
    @Deprecated /*Use buildList*/
    public OpaqueDataElementVO build() {
        return null;
    }

}
