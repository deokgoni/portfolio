package com.gon.webservice.repository;

import com.gon.webservice.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item){
        //id가 없다는 것은 1차캐쉬에 없으므로 등록
        if(item.getId() == null){
            em.persist(item);
        }else{
            //item이 있으면 강제 update 진행
            em.merge(item);
        }
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
