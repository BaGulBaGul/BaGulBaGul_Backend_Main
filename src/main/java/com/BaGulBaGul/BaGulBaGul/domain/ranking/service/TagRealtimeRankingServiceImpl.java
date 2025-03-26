package com.BaGulBaGul.BaGulBaGul.domain.ranking.service;

import com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.service.response.TagRankingItemInfo;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.repository.TagRankingRepository;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagRealtimeRankingServiceImpl implements TagRealtimeRankingService {

    private final TagRankingRepository tagRankingRepository;

    @Override
    public List<String> getTagRanking(int count) {
        return tagRankingRepository.getTopKTagsFrom7Days(count);
    }

    @Override
    public void increaseTagScore(Collection<TagRankingItemInfo> tags) {
        tagRankingRepository.increaseDayTags(LocalDateTime.now(), tags);
        tagRankingRepository.increase7DaysTags(tags);
    }

    @Override
    public void increaseTagScore1(Collection<String> tags) {
        increaseTagScore(
                tags.stream()
                        .map(tag -> new TagRankingItemInfo(tag, 1L))
                        .collect(Collectors.toList())
        );
    }
}
