package com.puppet.starter;

import com.puppet.starter.annotation.EnableKafkaProducer;
import com.puppet.starter.properties.KafkaProducerConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Конфигурация позволяет создать KafkaTemplate в зависимости от принятых настроек.
 * Укажите над полем с типом KafkaTemplate аннотацию KafkaTemplate @EnableKafkaProducer,
 * пропишите в аннотации необходимую конфигурацию, по умолчанию используются:
 * bootstrapServers() default "localhost:29092;localhost:29093;localhost:29094",
 * keySerializer() default KeyValueSerializer.STRING,
 * valueSerializer() default KeyValueSerializer.STRING,
 * batchSize() default "50";
 * И spring подгрузит к полю необходимый бин KafkaTemplate.
 */
@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfiguration implements BeanPostProcessor {

    private final ConfigurableListableBeanFactory beanFactory;

    /**
     * Метод настройки компонента перед инициализацией. Метод находит поля, помеченные
     * соответствующей аннотацией, создает KafkaTemplate, регистрирует в beanFactory
     * и устанавливает полю. В случе если в beanFactory уже имеется бин с точно такой же
     * конфигурацией, то вместо создания новой подгружает уже имеющийся.
     *
     * @param bean     класс бина перед инициализацией
     * @param beanName название бина
     * @return возвращает бин с установленными зависимостями KafkaTemplate
     * @throws BeansException в случае ошибки настройки бина укажет какую зависимость не удалось установить
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(EnableKafkaProducer.class)) {
                EnableKafkaProducer annotation = field.getAnnotation(EnableKafkaProducer.class);

                KafkaProducerConfigurationProperties properties = createKafkaProducerConfigurationProperties(annotation);
                String uniqueName = createUniqueName(annotation);
                KafkaTemplate<?, ?> myKafkaTemplate;

                if (beanFactory.containsBean(uniqueName)) {
                    myKafkaTemplate = beanFactory.getBean(uniqueName, KafkaTemplate.class);
                } else {
                    ProducerFactory<?, ?> producerFactory = myProducerFactory(properties,
                            keySerializerClass(properties),
                            valueSerializerClass(properties));

                    myKafkaTemplate = myKafkaTemplate(producerFactory);
                    beanFactory.registerSingleton(uniqueName, myKafkaTemplate);
                }

                field.setAccessible(true);
                try {
                    field.set(bean, myKafkaTemplate);
                } catch (IllegalAccessException e) {
                    throw new BeansException("Не удалось установить бин" + field.getName()) {
                    };
                }
            }
        }
        return bean;
    }

    /**
     * Создает уникальное имя для бина
     *
     * @param annotation приминает аннотацию с конфигурацией
     * @return уникальное имя
     */
    private String createUniqueName(EnableKafkaProducer annotation) {
        return annotation.bootstrapServers().concat("-")
                .concat(annotation.keySerializer().name())
                .concat("-").concat(annotation.valueSerializer().name())
                .concat("-").concat(annotation.batchSize())
                .concat("-").concat("Template");
    }

    /**
     * Метод создания экземпляра настроек KafkaTemplate из аннотации
     *
     * @param annotation приминает аннотацию с конфигурацией EnableKafkaProducer
     * @return KafkaProducerConfigurationProperties
     */
    private KafkaProducerConfigurationProperties createKafkaProducerConfigurationProperties(EnableKafkaProducer annotation) {
        return new KafkaProducerConfigurationProperties(
                annotation.bootstrapServers(),
                annotation.keySerializer(),
                annotation.valueSerializer(),
                annotation.batchSize());
    }

    /**
     * Метод получает класс сериализации для ключа, нужен для динамической настройки типа KafkaTemplate
     *
     * @param properties экземпляр с настройками KafkaProducerConfigurationProperties
     * @return класс сериализации
     */
    private Class keySerializerClass(KafkaProducerConfigurationProperties properties) {
        return properties.getKeySerializer().getSerializerClass();
    }

    /**
     * Метод получает класс сериализации для сообщений, нужен для динамической настройки типа KafkaTemplate
     *
     * @param properties экземпляр с настройками KafkaProducerConfigurationProperties
     * @return класс сериализации
     */
    private Class valueSerializerClass(KafkaProducerConfigurationProperties properties) {
        return properties.getValueSerializer().getSerializerClass();
    }

    /**
     * Метод создания ProducerFactory
     *
     * @param properties      экземпляр с настройками KafkaProducerConfigurationProperties
     * @param keySerializer   класс сериализации ключа
     * @param valueSerializer класс сериализации сообщений
     * @param <T>             может принимать String, Integer или ImageKafka
     * @param <S>             может принимать String, Integer или ImageKafka
     * @return экземпляр ProducerFactory
     */
    private <T, S> ProducerFactory<T, S> myProducerFactory(KafkaProducerConfigurationProperties properties,
                                                           Class<? extends Serializer<T>> keySerializer,
                                                           Class<? extends Serializer<S>> valueSerializer) {
        return new DefaultKafkaProducerFactory<>(Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers(),
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer,
                ProducerConfig.BATCH_SIZE_CONFIG, properties.getBatchSize()));
    }

    /**
     * Метод создания KafkaTemplate с динамической типизацией
     *
     * @param producer ProducerFactory
     * @param <T>      может принимать String, Integer или ImageKafka
     * @param <S>      может принимать String, Integer или ImageKafka
     * @return KafkaTemplate
     */
    private <T, S> KafkaTemplate<T, S> myKafkaTemplate(ProducerFactory<T, S> producer) {
        return new KafkaTemplate<>(producer);
    }
}
