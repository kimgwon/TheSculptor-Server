package backend.sculptor.domain.stone.service;

import backend.sculptor.domain.stone.entity.Stone;
import backend.sculptor.domain.stone.entity.StoneType;
import backend.sculptor.domain.stone.entity.Type;
import backend.sculptor.domain.stone.repository.*;
import backend.sculptor.domain.store.dto.WearType;
import backend.sculptor.global.exception.BadRequestException;
import backend.sculptor.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TypeService extends ProductService<Type, StoneType> {
    private final TypeRepository typeRepository;
    private final StoneTypeRepository stoneTypeRepository;
    private final StoneRepository stoneRepository;

    public TypeService(TypeRepository typeRepository, StoneTypeRepository stoneTypeRepository, StoneRepository stoneRepository) {
        super(typeRepository, stoneTypeRepository);
        this.typeRepository = typeRepository;
        this.stoneTypeRepository = stoneTypeRepository;
        this.stoneRepository = stoneRepository;
    }

    @Override
    protected BaseProductRepository<Type> getBaseProductRepository() {
        return typeRepository;
    }

    @Override
    protected BaseStoneProductRepository<StoneType> getBaseStoneProductRepository() {
        return stoneTypeRepository;
    }

    @Transactional
    public WearType.Response updateWearType(UUID stoneId, UUID typeId) {
        StoneType stoneType = stoneTypeRepository.findByStoneIdAndProductId(stoneId, typeId)
                .orElseThrow(() -> new BadRequestException(ErrorCode.STONE_NOT_PURCHASE_ITEM.getMessage()));

        Type type = stoneType.getStone().getType();
        Stone stone = stoneType.getStone();
        stone.wearType(type);
        stoneRepository.save(stone);

        return WearType.Response.builder()
                .stoneId(stoneId)
                .typeId(type.getId())
                .build();
    }
}
