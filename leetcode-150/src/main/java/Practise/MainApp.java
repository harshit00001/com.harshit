package Practise;

class SpellCheckerDaemon extends Thread
{
    @Override
    public void run()
    {
        if (isDaemon())
        {
            System.out.println("Spell Checker Daemon is running in the background...");
        }

        // Simulating background spell-check task
        while (true)
        {
            System.out.println("Checking spelling in background...");
            try
            {
                Thread.sleep(1000); // Pause for demonstration
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}

public class MainApp
{
    public static void main(String[] args) throws Exception
    {
        SpellCheckerDaemon spellChecker = new SpellCheckerDaemon();

        // Mark as daemon before starting
        spellChecker.setDaemon(true);

        spellChecker.start();

        System.out.println("Main thread: User is typing in the editor...");
        Thread.sleep(3000); // Simulate user typing

        System.out.println("Main thread work finished. JVM will exit now.");
    }
}