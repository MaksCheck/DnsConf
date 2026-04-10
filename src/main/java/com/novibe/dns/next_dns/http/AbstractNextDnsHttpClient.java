package com.novibe.dns.next_dns.http;

import com.novibe.common.HttpRequestSender;
import com.novibe.common.exception.DnsHttpError;
import com.novibe.common.exception.ProcessException;
import com.novibe.common.util.Log;

public abstract class AbstractNextDnsHttpClient extends HttpRequestSender {

    protected abstract String path();

    @Override
    protected String apiUrl() {
        return "https://api.nextdns.io/profiles/%s".formatted(dnsProfile.clientId());
    }

    @Override
    protected String authHeaderName() {
        return "X-Api-Key";
    }

    @Override
    protected String authHeaderValue() {
        return dnsProfile.authSecret();
    }

    @Override
    protected final void react401() {
        throw new ProcessException("Invalid api key");
    }

    @Override
    protected void react403() {
        throw new ProcessException("Invalid api key!");
    }

    @Override
    protected void react404(DnsHttpError dnsHttpError) {
        if (dnsProfile.clientId() != null) {
            Log.fail("Make sure your profile is registered via https://my.nextdns.io/signup. Default unregistered profile can't be configured");
        }
        throw dnsHttpError;
    }
}
