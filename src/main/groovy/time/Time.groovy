package time

import cli.annotations.Command
import cli.annotations.Run
import com.beust.jcommander.Parameter

/**
 * @author Shawn Hartsock
 */
@Command
class Time {

    @Parameter(
        names = [ "-f" , "--format" ] ,
        description="date format string using standard Java date formats "
    )
    private String format = "yyyy-MM-dd"

    @Run
    String run() {
        return new GregorianCalendar().format(format)
    }

}
