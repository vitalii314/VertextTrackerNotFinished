package ua.vertex;

import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

/**
 * Created by Дмитрий on 19.05.2016.
 */

@Configuration
@ComponentScan("ua.vertex")
@PropertySource("classpath:db.properties")
public class Conf {

    @Value("${url}")
    private String URL;
    @Value("${username}")
    private String USERNAME;
    @Value("${password}")
    private String PASSWORD;

    @Bean
    @Profile("dev")
    DataSource dataSource() {
        PGPoolingDataSource dataSource = new PGPoolingDataSource();
        dataSource.setUrl(URL);
        dataSource.setUser(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setInitialConnections(5);
        dataSource.setMaxConnections(15);
        return dataSource;

    }

    @Bean
    @Profile("test")
    DataSource testDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("classpath:embedded/create.sql")
                .addScript("classpath:embedded/insert.sql")
                .continueOnError(false)
                .build();
    }

    @Bean
    public static PropertyPlaceholderConfigurer properties() {
        PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
        ClassPathResource[] resources = new ClassPathResource[]
                {new ClassPathResource("db.properties")};
        ppc.setLocations(resources);
        ppc.setIgnoreUnresolvablePlaceholders(true);
        return ppc;
    }

}