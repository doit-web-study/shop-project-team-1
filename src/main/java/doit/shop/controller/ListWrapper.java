package doit.shop.controller;

import java.util.List;

public record ListWrapper<T>(
        List<T> result
) {
}
