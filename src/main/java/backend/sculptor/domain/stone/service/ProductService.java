package backend.sculptor.domain.stone.service;

import backend.sculptor.domain.stone.entity.Product;
import backend.sculptor.domain.stone.entity.StoneProduct;
import backend.sculptor.domain.stone.repository.BaseProductRepository;
import backend.sculptor.domain.stone.repository.BaseStoneProductRepository;
import backend.sculptor.global.exception.ErrorCode;
import backend.sculptor.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public abstract class ProductService<T1 extends Product, T2 extends StoneProduct> {
    private final BaseProductRepository<T1> baseProductRepository;
    private final BaseStoneProductRepository<T2> baseStoneProductRepository;

    public Boolean isProduct(UUID productId) {
        return baseProductRepository.findById(productId).isPresent();
    }

    public T2 purchase(T2 stoneProduct) {
        baseStoneProductRepository.save(stoneProduct);
        return stoneProduct;
    }

    public List<T1> getProductsById(List<UUID> productIds) {
        return productIds.stream()
                .map(this::getProductById)
                .toList();
    }

    public T1 getProductById(UUID productId) {
        return baseProductRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND.getMessage()));
    }

    public Boolean isPurchased(UUID stoneId, UUID productId) {
        return baseStoneProductRepository.findByStoneIdAndProductId(stoneId, productId).isPresent();
    }

    protected abstract BaseProductRepository<T1> getBaseProductRepository();
    protected abstract BaseStoneProductRepository<T2> getBaseStoneProductRepository();
}
