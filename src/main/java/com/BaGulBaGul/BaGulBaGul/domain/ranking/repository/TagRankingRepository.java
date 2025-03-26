package com.BaGulBaGul.BaGulBaGul.domain.ranking.repository;

import com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.service.response.TagRankingItemInfo;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public interface TagRankingRepository {
    void increase7DaysTags(Collection<? extends TagRankingItemInfo> tags);
    void decrease7DaysTags(Collection<? extends TagRankingItemInfo> tags);
    void deleteAll7DaysTags();
    List<String> getTopKTagsFrom7Days(int k);

    void increaseDayTags(LocalDateTime time, Collection<? extends TagRankingItemInfo> tags);
    void decreaseDayTags(LocalDateTime time, Collection<? extends TagRankingItemInfo> tags);
    Iterator<TagRankingItemInfo> getDayTagIterator(LocalDateTime time);
    void deleteAllDayTagsByTime(LocalDateTime time);
}
