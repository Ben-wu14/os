package process;

public class BlockReason {
	//����ԭ��
    public static final BlockReason TIMEOUT=new BlockReason();
    public static final BlockReason MEMFULL=new BlockReason();
    public static final BlockReason DEVICE_BUZY=new BlockReason();
    public static final BlockReason NONE=new BlockReason();
}
