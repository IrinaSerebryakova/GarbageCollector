import java.util.concurrent.TimeUnit;

/*
-Xmc256m
-Xmx256m
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=./logs/heapdump.hprof
-XX:+UseG1GC
-/logs:gc=debug:file=./logs/gc-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m
 */

public class MemoryDemoApplication {

	public static void main(String[] args) throws InterruptedException {
		var array = new byte[1_000_000_000];
		for (int i = 0; i < 1_000_000_000; i++) {
			array[i] = (byte) i;
			if (i % 10_000 == 0) {
				System.out.println("Пауза на 1 секунду");
				Thread.sleep(TimeUnit.SECONDS.toMillis(1));
			}
			if (i % 100_000 == 0) {
				System.out.println("Запуск GC");
				System.gc();
			}
		}
		System.out.println("Длина массива " + array.length);
	}
}