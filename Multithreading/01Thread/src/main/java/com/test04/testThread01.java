package com.test04;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class testThread01 extends Thread {
    //  共享数据
    static BigDecimal money = BigDecimal.valueOf(100.00);
    static int count = 3;

    //  最小的中奖金额
    static final BigDecimal MIN = BigDecimal.valueOf(0.01);

    @Override
    public void run() {
        synchronized (testThread01.class) {
            if (count == 0) {
                System.out.println(getName() + "没有抢到红包");
            } else {
                //  中奖金额
                BigDecimal prize;
                if (count == 1) {
                    //  表示最后一个红包，无需随机，剩余的所有钱都是中奖金额
                    prize = money;
                } else {
                    Random random = new Random();
                    double bounds = money.subtract(BigDecimal.valueOf(count-1).multiply(MIN)).doubleValue();
                    prize = BigDecimal.valueOf(random.nextDouble(bounds));
                }
                prize = prize.setScale(2, RoundingMode.HALF_UP);
                //  从money中去掉中奖的金额
                money = money.subtract(prize);
                count--;
                System.out.println(getName() + "抢到了" + prize + "元");
            }
        }
    }
}
