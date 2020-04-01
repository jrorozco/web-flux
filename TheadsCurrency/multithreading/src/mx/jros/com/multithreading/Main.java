package mx.jros.com.multithreading;

public class Main {

	public static void main(String[] args) throws InterruptedException {

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("We are now in thread " + Thread.currentThread().getName());
				System.out.println("Current thread priority is " + Thread.currentThread().getPriority());
			}});
		thread.setName("New worker Thread");
		thread.setPriority(Thread.MAX_PRIORITY);
		
		System.out.println("We are in thread : " + Thread.currentThread().getName() + " before starting a new Thread");
		thread.start();
		System.out.println("We are in thread : " + Thread.currentThread().getName() + " after starting a new Thread");
		
		Thread.sleep(10000);
	}

}
