//import com.cronutils.descriptor.CronDescriptor;
//import com.cronutils.model.definition.CronDefinition;
//import com.cronutils.model.definition.CronDefinitionBuilder;
//import com.cronutils.parser.CronParser;
//import com.project.scheduling.application.SchedulingRunnable;
//import com.project.scheduling.config.CronTaskRegistrar;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.util.Locale;
//
//import static com.cronutils.model.CronType.QUARTZ;
///**
// * @author : thucnh
// * @mailto : thucnh96.dev@gmail.com
// * @created :21/10/2021 - 1:30 PM
// */
//public class TaskTest {
//    @Autowired
//    CronTaskRegistrar cronTaskRegistrar;
//
//    @Test
//    public void testTask() throws InterruptedException {
//        SchedulingRunnable task = new SchedulingRunnable("demologTask", "taskNoParams", null);
//        cronTaskRegistrar.addCronTask(task, "0/10 * * * * ?");
//
//        // 便于观察 dsd
//        Thread.sleep(3000000);
//    }
//
//    @Test
//    public void testHaveParamsTask() throws InterruptedException {
//        SchedulingRunnable task = new SchedulingRunnable("demologTask", "taskWithParams", "haha", 23);
//        cronTaskRegistrar.addCronTask(task, "0/10 * * * * ?");
//
//        // 便于观察
//        Thread.sleep(3000000);
//    }
//    @Test
//    public void testStringConvert(){
//        try {
//            final String expr = "0 * * 1-3 * ? *";
//            CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(QUARTZ);
//            CronParser parser = new CronParser(cronDefinition);
//            Locale localeVN = new Locale("vi", "VN");
//            CronDescriptor descriptor = CronDescriptor.instance(localeVN);
//            String description = descriptor.describe(parser.parse(expr));
//        }catch (Exception e){
//           e.printStackTrace();
//        }
//
//    }
//}
