package xd.fw;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class JKN extends Conf {

    @PostConstruct
    public void readFromDb() {
        super.readFromDb();

        addPropertyChangeListener((configName)->{
            super.readFromDb();
        });
    }

    public static int batch_event = 100;

    //gold upgrade
    public static int gold_ucn = 1;
    public static int gold_acn = 0;

    //white upgrade
    public static int white_ucn = 10;
    public static int white_acn = 0;

    //diamond upgrade
    public static int diamond_ucn = 10;
    public static int diamond_acn = 30;

    public static int region_ucn = 20;
    public static int region_acn = 200;
    public static long region_consumed = 120000 * 100;//12w
    public static float region_settlement = 0.03f;

    public static int city_ucn = 35;
    public static int city_region_cn = 3;
    public static long city_consumed = 600000 * 100;//60w
    public static float city_settlement = 0.04f;

    public static float store_keeper_first_settlement = 0.03f;
    public static float store_keeper_extend_settlement = 0.03f;

    public static float store_order_first_settlement = 0.06f;
    public static float store_order_extend_settlement = 0.06f;

    public static float withdraw_percent = 0.9f;

    public static int membership_count = 5900;
    public static int vip_cost = 59000;

    public static float settlement_one = 0.07f;
    public static float settlement_two = 0.09f;
    public static float settlement_three = 0.11f;

    public static int settlement_period = 60000;

    public static int vip_discount = 80;

    public static String user_notify_url;

    public static String security_key;

    public static String sms_telephones;


}