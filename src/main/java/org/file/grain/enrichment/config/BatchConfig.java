package org.file.grain.enrichment.config;

import java.util.Arrays;

import org.file.grain.enrichment.model.Person;
import org.file.grain.enrichment.repository.PersonRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Bean
	public FlatFileItemReader<Person> reader() {
		return new FlatFileItemReaderBuilder<Person>()
				.name("personItemReader")
				.resource(new ClassPathResource("persons.csv"))
				.delimited()
				.names(Arrays.asList("firstName", "lastName", "email").toArray(new String[0]))
				.fieldSetMapper(fieldSet -> {
	                Person person = new Person();
	                person.setFirstName(fieldSet.readString("firstName"));
	                person.setLastName(fieldSet.readString("lastName"));
	                person.setEmail(fieldSet.readString("email"));
	                return person;
	            })
				.linesToSkip(1)
				.build();
	}
	

	@Bean
	public Job importUserJob(JobBuilderFactory jobBuilderFactory, Step step1) {
		return  jobBuilderFactory
			   .get("importUserJob")
			   .incrementer(new RunIdIncrementer())
			   .flow(step1)
			   .end()
			   .build();
	}

	@Bean
	public Step step1(StepBuilderFactory stepBuilderFactory, PersonRepository personRepository) {
		return stepBuilderFactory
			   .get("step1").<Person, Person>chunk(10)
			   .reader(reader()).processor(processor())
			   .writer(jpaConditionalWriter(personRepository)) // Use the conditional writer
			   .build();
	}

	@Bean
	public ItemProcessor<Person, Person> processor() {
		return person -> {
			person.setFirstName(person.getFirstName().toUpperCase());
			return person;
		};
	}

	@Bean
	public ItemWriter<Person> jpaConditionalWriter(PersonRepository personRepository) {
		return persons -> 
			persons.stream().filter(person -> !personRepository.existsByEmail(person.getEmail()))
					.forEach(personRepository::save);

	}

}
