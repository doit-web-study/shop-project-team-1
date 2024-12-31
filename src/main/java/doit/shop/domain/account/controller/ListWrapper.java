package doit.shop.domain.account.controller;

import java.util.List;

public record ListWrapper<T>(
        List<T> result
) {
}
