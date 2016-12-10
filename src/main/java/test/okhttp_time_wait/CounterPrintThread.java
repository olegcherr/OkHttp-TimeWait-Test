package test.okhttp_time_wait;

import java.util.concurrent.atomic.AtomicInteger;


public class CounterPrintThread extends Thread
{
	private AtomicInteger counter;

	public CounterPrintThread(AtomicInteger counter) {
		this.counter = counter;
	}

	public void run() {
		long loopStartTime = System.currentTimeMillis();

		try {
			while (true) {
				sleep(1000);

				long time = System.currentTimeMillis();
				long diff = counter.getAndSet(0);

				int res = (int) (((float) diff) / ((time - loopStartTime) / 1000f));
				System.out.println(res + " req/sec");
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
