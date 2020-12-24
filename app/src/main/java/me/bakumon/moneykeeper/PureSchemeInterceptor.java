/*
 * Copyright 2018 Bakumon. https://github.com/Bakumon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package me.bakumon.moneykeeper;

import android.support.annotation.NonNull;

import me.drakeet.floo.Chain;
import me.drakeet.floo.Interceptor;

/**
 * Floo 路由 scheme 拦截器，修正 debug 下的 scheme
 * @author Bakumon https://bakumon.me
 */
public class PureSchemeInterceptor implements Interceptor {
    private @NonNull
    final String scheme;

    public PureSchemeInterceptor(@NonNull String scheme) {
        this.scheme = scheme;
    }

    @NonNull
    @Override
    public Chain intercept(@NonNull Chain chain) {
        if (BuildConfig.DEBUG && Router.SCHEME.equals(chain.request().getScheme())) {
            chain = new Chain(chain.request().buildUpon().scheme(scheme).build());
        }
        return chain;
    }
}
