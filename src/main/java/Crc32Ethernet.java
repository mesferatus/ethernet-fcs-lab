public final class Crc32Ethernet {
    private Crc32Ethernet() { }

    public static long calculate(byte[] data) {
        long crc = 0xFFFFFFFFL;
        for (byte value : data) {
            crc ^= value & 0xFFL;
            for (int bit = 0; bit < 8; bit++) {
                crc = (crc & 1L) != 0
                        ? (crc >>> 1) ^ 0xEDB88320L
                        : crc >>> 1;
            }
        }
        return (crc ^ 0xFFFFFFFFL) & 0xFFFFFFFFL;
    }

    public static String hex(long value) {
        return String.format("%08X", value);
    }
}
