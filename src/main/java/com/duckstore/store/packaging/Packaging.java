package com.duckstore.store.packaging;

import java.util.List;

public record Packaging(PackageType type, List<Protection> protections) {}
