package com.tiurinvalery.springdata.sdk2.service;

import com.tiurinvalery.springdata.sdk2.annotations.Attribute;
import com.tiurinvalery.springdata.sdk2.annotations.Key;
import com.tiurinvalery.springdata.sdk2.annotations.Table;
import com.tiurinvalery.springdata.sdk2.entities.Item;
import com.tiurinvalery.springdata.sdk2.parser.data.KeyProperties;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.GenericWebApplicationContext;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ClassLoaderServiceImpl {

    @Value("${spring.data.dynamodb.sdkv2.packages.entities}")
    private String entities;

    @Value("${spring.data.dynamodb.sdkv2.packages.repositories}")
    private String repositories;

    @Autowired
    private GenericWebApplicationContext context;

    private Set<Class> entityClasses;

    private Set<Class> repositoryClasses;

    private List<Item> entityItems;

    public List<Item> getEntityItems() {
        return entityItems;
    }

    @PostConstruct
    public void init() {
        createClassesSet();
        createItemList();

    }

    public Set<Class> getEntityClasses() {
        return entityClasses;
    }

    public void createClassesSet() {
        entityClasses = new HashSet<>();
        repositoryClasses = new HashSet<>();

        //Create entity classes set
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                return super.isCandidateComponent(beanDefinition) || beanDefinition.getMetadata().isAbstract() || beanDefinition.getMetadata().isInterface();
            }
        };
        scanner.addIncludeFilter(new AnnotationTypeFilter(Table.class));
//        scanner.addIncludeFilter(new AnnotationTypeFilter(CrudRepository.class));


        for (BeanDefinition beanDefinition : scanner
                .findCandidateComponents(entities)) {
            try {
                entityClasses.add(Class.forName(beanDefinition.getBeanClassName()));
            } catch (ClassNotFoundException cnfe) {
                System.out.println(cnfe);
            }
        }

//        for (BeanDefinition beanDefinition : scanner
//                .findCandidateComponents(repositories)) {
//            try {
//                repositoryClasses.add(Class.forName(beanDefinition.getBeanClassName()));
//            } catch (ClassNotFoundException cnfe) {
//                System.out.println(cnfe);
//            }
//        }
    }

    void createItemList() {
        final List<Item> itemList = new LinkedList<>();
        for (Class c : entityClasses) {
            Field[] fields = c.getDeclaredFields();
            Item item = new Item();
            item.setClazz(c);
            try {
                Table table = (Table) c.getAnnotationsByType(Table.class)[0];
                item.setTableName(table.tableName());
            } catch (NullPointerException npe) {
                System.out.println(npe);
            }
            Map<String, String> fieldMappingMap = new HashMap<>();
            Key key = null;
            for (Field f : fields) {
                try {
                    if (null == key) {
                        key = f.getAnnotationsByType(Key.class)[0];
                        item.setKeys(Map.of(f.getName(), KeyProperties.builder().fieldName(key.fieldName()).keyType(key.keyType()).build()));
                    }
                } catch (NullPointerException npe) {
                    System.out.println(npe);
                }
                try {
                    Attribute attribute = f.getAnnotationsByType(Attribute.class)[0];
                    fieldMappingMap.put(f.getName(), attribute.name());
                } catch (NullPointerException npe) {
                    System.out.println(npe);
                }
            }
            item.setCodeAndDbFieldMapping(fieldMappingMap);
            itemList.add(item);
        }

        entityItems = itemList;

        final List<com.tiurinvalery.springdata.sdk2.entities.Repository> repositories = new LinkedList<>();
        for (Class c : repositoryClasses) {

            c.getGenericSuperclass();
        }

    }

    public Set<Class> getRepositoryClasses() {
        return repositoryClasses;
    }
}
