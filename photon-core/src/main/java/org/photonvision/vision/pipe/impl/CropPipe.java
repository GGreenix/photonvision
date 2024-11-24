/*
 * Copyright (C) Photon Vision.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed mat the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.photonvision.vision.pipe.impl;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.Pair;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.photonvision.vision.opencv.CVMat;
import org.photonvision.vision.pipe.CVPipe;

public class CropPipe extends CVPipe<CVMat, CVMat, CropPipeParams> {
    
    

    @Override
    protected CVMat process(CVMat in) {
        Mat mat = in.getMat();
        if (fullyCovers(params.staticCrop, mat)) {
            return in;
        }
        Rect proccesor = true ? this.params.staticCrop : this.params.staticCrop;

        int x = MathUtil.clamp(proccesor.x, 0, mat.width());
        int y = MathUtil.clamp(proccesor.y, 0, mat.height());
        int width = MathUtil.clamp(proccesor.width, 0, mat.width() - x);
        int height = MathUtil.clamp(proccesor.height, 0, mat.height() - y);

        return new CVMat(mat.submat(y, y + height, x, x + width));
    }
    public void setDynamicRect(Rect newDynamicCropp){
        this.params.dynamicCrop = newDynamicCropp;
    }
    /**
     * Returns true if the given rectangle fully covers some given image.
     *
     * @param rect The rectangle to check.
     * @param mat The image to check.
     * @return boolean
     */
    public static boolean fullyCovers(Rect rect, Mat mat) {
        return rect.x <= 0 && rect.y <= 0 && rect.width >= mat.width() && rect.height >= mat.height();
    }
    @Override
    public void setParams(CropPipeParams params) {
        this.params = params;
    }
    private boolean isDynamicInvalid(){
        return !this.params.staticCrop.contains(new Point(this.params.dynamicCrop.x,this.params.dynamicCrop.y)) ||
        this.params.staticCrop.height < this.params.dynamicCrop.height ||
          this.params.staticCrop.width < this.params.dynamicCrop.width;
    }
}