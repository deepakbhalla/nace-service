package com.nace.unit.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.nace.entities.NaceDetailsEntity;
import com.nace.repositories.NaceDetailsRepository;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
public class NaceDetailsRepositoryTest {

    @Autowired
    private NaceDetailsRepository naceDetailsRepository;

    private List<NaceDetailsEntity> naceRecordsToBePersisted;

    @BeforeAll
    public void setUp() {
        naceRecordsToBePersisted = new ArrayList<>();
        naceRecordsToBePersisted.add(NaceDetailsEntity.builder().code("11").description("description_1").id(1L)
                .itemAlsoIncludes("itemAlsoIncludes_1").itemIncludes("itemIncludes_1").itemExcludes("itemExcludes_1")
                .order(100L).build());
        naceRecordsToBePersisted.add(NaceDetailsEntity.builder().code("22").description("description_2").id(2L)
                .itemAlsoIncludes("itemAlsoIncludes_2").itemIncludes("itemIncludes_2").itemExcludes("itemExcludes_2")
                .order(200L).build());
    }

    @Test
    public void testSaveAllNaceDetails() {

        naceDetailsRepository.saveAll(naceRecordsToBePersisted);

        List<NaceDetailsEntity> result = naceDetailsRepository.findByOrder(100L);
        assertEquals(result.get(0).getDescription(), "description_1");
    }

    @Test
    public void testDeleteByOrder() {

        naceDetailsRepository.saveAll(naceRecordsToBePersisted);

        naceDetailsRepository.deleteByOrder(200L);
        List<NaceDetailsEntity> findById = naceDetailsRepository.findByOrder(200L);
        assertEquals(findById.size(), 0);
    }
}
