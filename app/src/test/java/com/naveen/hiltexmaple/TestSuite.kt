package com.naveen.hiltexmaple

import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * Comprehensive test suite for all features in the Hilt Examples app.
 * 
 * This test suite includes:
 * - API implementation tests (data models, network exceptions, repository, viewmodel)
 * - Hilt dependency injection tests (modules, legacy features)
 * - Clean architecture validation tests
 * - CRUD operations tests
 * - Error handling tests
 * - State management tests
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(
    // API Implementation Tests
    com.naveen.hiltexmaple.api.data.model.UserTest::class,
    com.naveen.hiltexmaple.api.data.model.ApiResponseTest::class,
    com.naveen.hiltexmaple.api.domain.model.UserStateTest::class,
    com.naveen.hiltexmaple.api.data.remote.NetworkExceptionTest::class,
    com.naveen.hiltexmaple.api.data.repository.UserRepositoryImplTest::class,
    com.naveen.hiltexmaple.api.ui.viewmodel.UserViewModelTest::class,
    
    // Hilt Module Tests
    com.naveen.hiltexmaple.api.di.NetworkModuleTest::class,
    com.naveen.hiltexmaple.api.di.RepositoryModuleTest::class,
    
    // Legacy Hilt Feature Tests
    com.naveen.hiltexmaple.dataClass.DataClassModuleTest::class,
    com.naveen.hiltexmaple.usingInterface.InterfaceModuleTest::class,
    com.naveen.hiltexmaple.veiwModule.ViewModelModuleTest::class,
    com.naveen.hiltexmaple.runTime.AssistedViewModelTest::class
)
class TestSuite
