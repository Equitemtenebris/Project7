package sprint7.generators;

import sprint7.models.Courier;

import static sprint7.utils.Utils.randomString;

public class CourierGenerator {

    public static Courier randomCourier() {
        return new Courier()
                .withLogin(randomString(8))
                .withPassword(randomString(12))
                .withFirstName(randomString(20));
    }
}