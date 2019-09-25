package scc.server.util;

import org.springframework.stereotype.Component;

/**
 * twitter雪花算法
 */
@Component
public class SnowflakeIdGenertor {
    // ==============================Fields===========================================
    /** 开始时间截 (2015-01-01) */
    private final long twepoch = 1420041600000L;
    //(2010-01-01)
//    private final long twepoch = 1262275200000L;

    /** 资源类型id所占的位数 */
    private final long resTypeIdits = 14L;

    /** 本地网id所占的位数 */
    private final long regionIdBits = 6L;

    /** 机器设备id所占的位数 */
    private final long machineIdBits = 2L;

    /** 支持的最大资源类型id，结果是 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数) */
    private final long maxResTypeId = -1L ^ (-1L << resTypeIdits);

    /** 支持的最大数据本地网id，结果是31 */
    private final long maxRegionId = -1L ^ (-1L << regionIdBits);

    /** 支持的最大数据机器设备ID，结果是31 */
    private final long maxMachineId = -1L ^ (-1L << machineIdBits);

    /** 序列在id中占的位数 */
    private final long sequenceBits = 3L;

    /** 机器ID向左移3位 */
    private final long machineIdShift = sequenceBits;

    /** 资源规格ID向左移5位(3+2) */
    private final long resTypeIdShift = sequenceBits + machineIdBits;

    /** 本地网ID向左移19位(3+2+14) */
    private final long regionIdShift = sequenceBits + machineIdBits + resTypeIdits;

    /** 时间截向左移25位(3+2+14+6) */
    private final long timestampLeftShift = sequenceBits + machineIdBits + resTypeIdits + regionIdBits;

    /** 生成序列的掩码，这里为7 */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /** 本地网ID(0~63)*/
    private long regionId;

    /** 资源类型ID(0~16383) */
    private long resTypeId;

    /** 机器设备ID(0~3) */
    private long machineId;

    /** 毫秒内序列(0~7) */
    private long sequence = 0L;

    /** 上次生成ID的时间截 */
    private long lastTimestamp = -1L;

    //==============================Constructors=====================================
    public SnowflakeIdGenertor(){}
    /**
     * 构造函数
     * @param resTypeId 资源类型ID
     * @param machineId 机器ID
     * @param regionId 本地网
     */
    public SnowflakeIdGenertor(long resTypeId, long machineId, long regionId) {
        if (regionId > maxRegionId || regionId < 0) {
            throw new IllegalArgumentException(String.format("region Id can't be greater than %d or less than 0", maxRegionId));
        }
        if (resTypeId > maxResTypeId || resTypeId < 0) {
            throw new IllegalArgumentException(String.format("resTypeId can't be greater than %d or less than 0", maxResTypeId));
        }
        if (machineId > maxMachineId || machineId < 0) {
            throw new IllegalArgumentException(String.format("machine Id can't be greater than %d or less than 0", maxMachineId));
        }
        this.regionId = regionId;
        this.resTypeId = resTypeId;
        this.machineId = machineId;
    }

    // ==============================Methods==========================================

    /**
     * 获得下一个ID (该方法是线程安全的)
     * @return
     */
    public synchronized  long nextId() {
        long timestamp = timeGen();
        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            //毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }
        //上次生成ID的时间截
        lastTimestamp = timestamp;
        //移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - twepoch) << timestampLeftShift)
                | (regionId << regionIdShift)
                | (resTypeId << resTypeIdShift)
                | (machineId << machineIdShift)
                | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }
    private String lpad(String value, int maxLength){
        StringBuffer sb = new StringBuffer(value);
        for(int i = 0; i < (maxLength - value.length());i++){
            sb.insert(0, "0");
        }
        return sb.toString();
    }

}
