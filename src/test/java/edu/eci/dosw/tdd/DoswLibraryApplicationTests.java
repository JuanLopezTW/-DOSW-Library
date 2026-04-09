package edu.eci.dosw.tdd;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("mongo")
@TestPropertySource(properties = {
		"spring.data.mongodb.uri=mongodb://localhost:27017/library-test",
		"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration,org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration",
		"spring.data.jpa.repositories.enabled=false",
		"spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults=false"
})
class DoswLibraryApplicationTests {

	@Test
	void contextLoads() {
	}
}