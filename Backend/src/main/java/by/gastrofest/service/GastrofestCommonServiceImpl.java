package by.gastrofest.service;

import by.gastrofest.mapper.GastrofestMapper;
import by.gastrofest.parser.model.GastroFest;
import by.gastrofest.parser.service.GastrofestCommonService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GastrofestCommonServiceImpl implements GastrofestCommonService {

    private final GastrofestMapper gastrofestMapper;

    private final GastrofestService gastrofestService;

    @Override
    public boolean shouldContinue(@NotNull final GastroFest gastroFest) {
        if (gastrofestService.existsByStartDate(gastroFest.getStartDate())) {
            return false;
        }
        final var gastroFestDbo = gastrofestService.save(gastrofestMapper.toGastroFestDbo(gastroFest));
        gastroFest.setId(gastroFestDbo.getId());
        return true;
    }
}
