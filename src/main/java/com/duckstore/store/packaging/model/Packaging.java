package com.duckstore.store.packaging.model;

import com.duckstore.store.packaging.enums.PackageType;
import com.duckstore.store.packaging.enums.ProtectionType;

import java.util.List;

public record Packaging(PackageType type, List<ProtectionType> protections) {}
