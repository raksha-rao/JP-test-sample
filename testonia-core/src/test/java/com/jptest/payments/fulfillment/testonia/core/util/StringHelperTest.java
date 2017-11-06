package com.jptest.payments.fulfillment.testonia.core.util;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

public class StringHelperTest {

    @Test
    public void testConvertStringToMap() {
        String content = "Shipping_calculation_mode=FlatRate&mc_gross=10.00&invoice=18708190646763232&auth_exp=12%3A41%3A03+Sep+01%2C+2016+PDT&protection_eligibility=Eligible"
                + "&address_status=unconfirmed&item_number1=item_number&payer_id=F49H3UZLXJ33U&tax=0.00&address_street=6530078+Bates+Lane%0D%0A8602255+Elizabeth+Court&payment_date=12%3A41%3A03+Aug+02%2C+2016+PDT"
                + "&payment_status=Pending&charset=windows-1252&shipping_option_name=shipping_option_name&address_zip=96799&mc_shipping=0.00&mc_handling=0.00&first_name=Carmela&transaction_entity=auth"
                + "&address_country_code=US&address_name=Carmela+Arthur&ify_version=3.8&insurance_option_selected=1&custom=custom&payer_status=verified"
                + "&business=saurijoshi-AuthCaptureWithCCBufsBankCompletion_testAllAPI-STAGE2T7271-20160802-123751-226-recipient%40jptest.com&address_country=United+States&shipping_option_amount=0.00"
                + "&num_cart_items=1&mc_handling1=0.00&address_city=Pago+Pago&gift_message=gift_message&verify_sign=AFcWxV21C7fd0v3bYYYRCpSSRl31As0OYAh4Wi3iuAN3LdGP.M9RI7B1"
                + "&payer_email=saurijoshi-AuthCaptureWithCCBufsBankCompletion_testAllAPI-STAGE2T7271-20160802-123751-226-sender%40jptest.com&mc_shipping1=0.00&tax1=0.00&btn_id1=123&parent_txn_id="
                + "&txn_id=9BN868076M615042W&payment_type=instant&remaining_settle=10&auth_id=9BN868076M615042W&last_name=Arthur&address_state=AS&item_name1=item_name"
                + "&receiver_email=saurijoshi-AuthCaptureWithCCBufsBankCompletion_testAllAPI-STAGE2T7271-20160802-123751-226-recipient%40jptest.com&auth_amount=10.00&shipping_discount=0.04&quantity1=1"
                + "&insurance_amount=0.03&receiver_id=46XL3U3QEMSRU&pending_reason=authorization&txn_type=express_checkout&discount=0.05&mc_gross_1=10.00&mc_currency=USD&residence_country=US&shipping_method=Default"
                + "&transaction_subject=Payment+for+send_money+flow&payment_gross=10.00&auth_status=Pending&shipping_is_default=1&ipn_track_id=9d13c58a10d2f";

        StringHelper helper = new StringHelper();
        Map<String, String> map = helper.convertStringToMap(content);

        Assert.assertTrue(map.size() > 0);
    }
}
