package com.tiurinvalery.springdata.sdk2.service;

import com.tiurinvalery.springdata.sdk2.annotations.Table;
import com.tiurinvalery.springdata.sdk2.entities.Item;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.tiurinvalery.springdata.sdk2.parser.EntityParser.defineKeyAndAttribute;
import static com.tiurinvalery.springdata.sdk2.parser.EntityParser.defineTableName;

@Service
public class ClassLoaderServiceImpl {

    @Value("${spring.data.dynamodb.sdkv2.packages.entities}")
    private String entities;

    private Set<Class> entityClasses;

    private List<Item> entityItems;

    public List<Item> getEntityItems() {
        return entityItems;
    }

    @PostConstruct
    public void init() {
        createClassesSet();
        createItemList();
    }


    public void createClassesSet() {
        entityClasses = new HashSet<>();

        //Create entity classes set
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                return super.isCandidateComponent(beanDefinition) || beanDefinition.getMetadata().isAbstract() || beanDefinition.getMetadata().isInterface();
            }
        };
        scanner.addIncludeFilter(new AnnotationTypeFilter(Table.class));


        for (BeanDefinition beanDefinition : scanner
                .findCandidateComponents(entities)) {
            try {
                entityClasses.add(Class.forName(beanDefinition.getBeanClassName()));
            } catch (ClassNotFoundException cnfe) {
                System.out.println(cnfe);
            }
        }

    }

    void createItemList() {
        final List<Item> itemList = new LinkedList<>();
        for (Class c : entityClasses) {
            Field[] fields = c.getDeclaredFields();
            Item item = new Item();

            defineTableName(c, item);
            defineKeyAndAttribute(itemList, fields, item);
        }

        entityItems = itemList;
    }

}
