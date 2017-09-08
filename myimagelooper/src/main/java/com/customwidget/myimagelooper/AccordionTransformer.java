/*
 * Copyright 2014 Toxic Bakery
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.customwidget.myimagelooper;

import android.view.View;

public class AccordionTransformer extends ABaseTransformer {

    @Override
    protected void onTransform(View page, float position) {
        page.setPivotX(position < 0 ? 0 : page.getWidth());
        if (position < -1) {//[-infinity,-1)
            page.setPivotX(page.getWidth());
        }else if (position < 0) {//[-1,0)
            page.setPivotX(0);
        }else {//[0,+infinity)
            page.setPivotX(page.getWidth());
        }
        page.setScaleX(position < 0 ? 1f + position : 1f - position);
    }
}
