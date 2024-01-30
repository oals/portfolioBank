package com.project.bank.service;

import com.project.bank.dto.HistoryDTO;

import com.project.bank.dto.PageRequestDTO;
import com.project.bank.dto.PageResponseDTO;
import com.project.bank.entity.History;
import com.project.bank.entity.QHistory;
import com.project.bank.repository.HistoryRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService{

    private final HistoryRepository historyRepository;
    private final ModelMapper modelMapper;

    @PersistenceContext
    EntityManager em;

    @Override

    public PageResponseDTO<HistoryDTO> SelectHistory(String accountNumber, String opt, PageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageable();


        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QHistory qHistory = QHistory.history;

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(opt.equals("전체")){
            booleanBuilder.and(qHistory.account.accountNumber.eq(accountNumber)
                    .and(qHistory.opt.ne("적금"))
                    .and(qHistory.opt.ne("적금 반환")));
        }else if (opt.equals("입금") || opt.equals("송금")){
            booleanBuilder.and(qHistory.account.accountNumber.eq(accountNumber)
                    .and(qHistory.opt.eq(opt)));
        }else if(opt.equals("적금")){
            booleanBuilder.and(qHistory.account.accountNumber.eq(accountNumber)
                    .and(qHistory.opt.contains(opt).or(qHistory.opt.eq("적금 반환")))
                    );
        }


        JPAQuery<History> query =  queryFactory.selectFrom(qHistory)
                    .where(booleanBuilder)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(qHistory.updateDate.desc());      //계좌 여러개일떄 문제  -> 계좌번호 칼럼 추가?



        List<HistoryDTO> historyDTO = new ArrayList<>();

        if(query.fetchAll() != null) {
            historyDTO = query.fetchAll().stream().map(x -> entityToDTO(x)).collect(Collectors.toList());
        }

        Long count = queryFactory
                .select(qHistory.count())
                .from(qHistory)
                .where(booleanBuilder)
                .fetchOne();


        return PageResponseDTO.<HistoryDTO>widthAll()
                .list(historyDTO)
                .pageRequestDTO(pageRequestDTO)
                .total(Integer.parseInt(count.toString()))
                .build();
    }
}
