package br.com.bittrexbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.bittrexbot.model.LocalHistory;

public interface LocalHistoryRepository extends JpaRepository<LocalHistory, Long>{

	@Query("select l "
		 + "  from LocalHistory l "
		 + " where l.marketName = :marketName "
		 + "   and l.strDtAddrow = :dateStringParam")
	public LocalHistory findByDate(@Param("marketName") String marketName, @Param("dateStringParam") String dateStringParam);
	
}
