package com.store.gift.service;

import com.store.gift.dao.CertificateDao;
import com.store.gift.dao.CertificateDaoImpl;
import com.store.gift.exception.CertificateAlreadyExistsException;
import com.store.gift.mapper.CertificateMapper;
import com.store.gift.mapper.TagMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CertificateServiceExceptionTest.class})
class CertificateServiceExceptionTest {

    private final static Exception cause = new Exception("Cause Exception");
    @Mock
    private final CertificateDao dao = mock(CertificateDaoImpl.class);
    @Mock
    private CertificateMapper mapper = mock(CertificateMapper.class);
    @Mock
    private TagMapper tagMapper = mock(TagMapper.class);
    @InjectMocks
    private CertificateService service = new CertificateServiceImpl(dao, mapper, tagMapper);
    private static final String message = "An error occurred";

    @Test
    void testRuntimeException() {
        try {
            throw new RuntimeException(message);
        } catch (RuntimeException e) {
            Assertions.assertEquals(message, e.getMessage());
        }
    }

    @Test
    void testRuntimeExceptionWithMessageAndCause() {
        Throwable cause = new IllegalArgumentException(message);
        RuntimeException e = new RuntimeException(message, cause);
        assertEquals(message, e.getMessage());
        assertEquals(cause, e.getCause());
    }

    @Test
    void testRuntimeExceptionThrownWithMessage() {
        try {
            throw new RuntimeException(message);
        } catch (RuntimeException ex) {
            assertEquals(message, ex.getMessage());
        }
    }

    @Test
    void testCertificateIsExistsException() {
        CertificateAlreadyExistsException exception =
                new CertificateAlreadyExistsException("Test Certificate");
        service.getByOrderId(1L);
        assertTrue(exception.getMessage().contains("Test Certificate"));
    }

    @Test
    @DisplayName("Creating RuntimeException with cause")
    void createRuntimeExceptionWithCause() {
        RuntimeException exception = new RuntimeException(cause);
        assertThrows(RuntimeException.class, () -> {
            throw exception;
        }, cause.getMessage());
    }
}
