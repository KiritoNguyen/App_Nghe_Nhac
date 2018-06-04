package application; 

import javafx.util.Duration;

public class Time_counter {
	Duration elapsed = new Duration(0);
    long start_ticks = 0;

    public void start()
    {
        start_ticks = System.currentTimeMillis();
    }

    public void stop()
    {
        elapsed = new Duration(0);
        start_ticks = 0;
    }

    public void pause()
    {
        elapsed = getTicks();
        start_ticks = 0;
    }

    public void seek(Duration argument)
    {
        elapsed = argument;
        if(start_ticks != 0)
            start_ticks = System.currentTimeMillis();
    }

    public Duration getTicks()
    {
        Duration temp = new Duration(0); 
        if(start_ticks == 0)
            if(elapsed.equals(temp))
                return temp;
            else
                return elapsed;
        return elapsed.add(new Duration((double)System.currentTimeMillis() - start_ticks));
    };
}
