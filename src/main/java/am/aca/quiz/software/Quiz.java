package am.aca.quiz.software;


import am.aca.quiz.software.entity.UserEntity;
import am.aca.quiz.software.entity.enums.Role;
import am.aca.quiz.software.service.ThreadService;
import am.aca.quiz.software.service.implementations.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;


/**
 * Created by
 * Tigran Gyozalyan,
 * Aram Yeghizaryan,
 * Edgar Ohanyan
 * 2019 February
 *
 *
 *
 * Refer to readMe for instructions
 */


@SpringBootApplication
@EnableAsync
@EnableScheduling
public class Quiz implements CommandLineRunner {


    @Autowired
    private ThreadService threadService;

    @Autowired
    private UserServiceImp userServiceImp;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(Quiz.class, args);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(8);
    }


    @Bean("threadPoolTaskExecutor")
    public TaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(1000);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("Async-");
        return executor;
    }

    @Bean
    public void adminUser() {

        List<Long> admins=userServiceImp.findAdminsIfExist();
        if(admins.isEmpty()) {
            UserEntity admin = new UserEntity();
            admin.setName("admin");
            admin.setSurname("admin");
            admin.setEmail("admin@admin.com");
            admin.setPassword(passwordEncoder.encode("adminadmin"));
            admin.setNickname("admin");
            admin.setActive(true);
            admin.setRoles(Collections.singleton(Role.ADMIN));
            userServiceImp.updateUser(admin);
        }else{
            try {
                userServiceImp.removeById(userServiceImp.findByEmail("admin@admin.com").getId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void run(String... args) throws Exception {
        threadService.findUser();
    }
}
