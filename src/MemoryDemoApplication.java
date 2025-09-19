import java.util.ArrayList;
import java.util.List;
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

		// Выделение большого массива, как в исходном примере
		var array = new byte[1_000_000_000];

        // Список для хранения ссылок на объекты, которые будут перемещены в старшее поколение
		List<byte[]> retainedObjects = new ArrayList<>();

		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			// Запись в большой массив
			array[i] = 1;

			// Создание нового объекта (1KB массива) в Eden
			byte[] edenObject = new byte[1024]; // 1KB

			// По желанию можно удерживать некоторые объекты для предотвращения их сборки
			if (i % 50_000 == 0) {
				retainedObjects.add(edenObject); // Удерживаем объект
				// Ограничиваем размер списка, чтобы не заполнить старшее поколение
				if (retainedObjects.size() > 1000) {
					retainedObjects.remove(0);
				}
			}

			// Каждые 10_000 итераций делаем паузу
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