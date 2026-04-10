package com.novibe.dns.cloudflare.http;

import com.novibe.common.HttpRequestSender;
import com.novibe.common.exception.DnsHttpError;
import com.novibe.common.exception.ProcessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestCloudflare extends HttpRequestSender {

    @Override
    protected String apiUrl() {
        return "https://api.cloudflare.com/client/v4/accounts/%s/gateway".formatted(dnsProfile.clientId());
    }

    @Override
    protected String authHeaderName() {
        return "Authorization";
    }

    @Override
    protected String authHeaderValue() {
        return "Bearer " + dnsProfile.authSecret();
    }

    @Override
    protected final void react401() {
        throw new ProcessException("Invalid API Token!");
    }

    @Override
    protected void react403() {
        throw new ProcessException("""
                Token doesn't have necessary permissions!
                Generate a token with permissions:
                1) Zero Trust:Edit
                2) Account Firewall Access Rules:Edit""");
    }

    @Override
    protected void react404(DnsHttpError httpError) {
        throw httpError;
    }

}
