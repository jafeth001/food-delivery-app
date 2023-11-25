package com.backend.foodproject.reposiory;

import com.backend.foodproject.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token,Long> {
    @Query("""
            select t from Token t inner join User u on t.user.id = u.id
            where u.id=:userId and (t.expired=false or t.revoked=false)
            """)
    List<Token> findAllValidTokensByUser(Long userId);

    @Query("""
            select t from Token t inner join seller u on t.seller.id = u.id
            where u.id=:sellerId and (t.expired=false or t.revoked=false)
            """)
    List<Token> findAllValidTokensBySelller(Long sellerId);

    Optional<Token> findByToken(String token);
}
