/*
 * ------------------------------------------------------------------------
 * Max chat Bot API
 * ------------------------------------------------------------------------
 * Copyright (C) 2025 COMMUNICATION PLATFORM LLC
 * ------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ------------------------------------------------------------------------
 */

package ru.max.botapi.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import javax.validation.Valid;

import org.jetbrains.annotations.Nullable;

/**
 * ContactAttachmentPayload
 */
public class ContactAttachmentPayload implements MaxSerializable {

    @Nullable
    private @Valid String vcfInfo;
    @Nullable
    private @Valid User maxInfo;

    public ContactAttachmentPayload vcfInfo(@Nullable String vcfInfo) {
        this.setVcfInfo(vcfInfo);
        return this;
    }

    /**
    * User info in VCF format
    * @return vcfInfo
    **/
    @Nullable
    @JsonProperty("vcf_info")
    public String getVcfInfo() {
        return vcfInfo;
    }

    @JsonProperty("vcf_info")
    @JsonAlias({"vcfInfo"})
    public void setVcfInfo(@Nullable String vcfInfo) {
        this.vcfInfo = vcfInfo;
    }

    public ContactAttachmentPayload maxInfo(@Nullable User maxInfo) {
        this.setMaxInfo(maxInfo);
        return this;
    }

    /**
    * User info
    * @return maxInfo
    **/
    @Nullable
    @JsonProperty("max_info")
    public User getMaxInfo() {
        return maxInfo;
    }

    @JsonProperty("max_info")
    @JsonAlias({"maxInfo", "tamInfo"})
    public void setMaxInfo(@Nullable User maxInfo) {
        this.maxInfo = maxInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (o == null || getClass() != o.getClass()) {
          return false;
        }

        ContactAttachmentPayload other = (ContactAttachmentPayload) o;
        return Objects.equals(this.vcfInfo, other.vcfInfo) &&
            Objects.equals(this.maxInfo, other.maxInfo);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (vcfInfo != null ? vcfInfo.hashCode() : 0);
        result = 31 * result + (maxInfo != null ? maxInfo.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ContactAttachmentPayload{"
            + " vcfInfo='" + vcfInfo + '\''
            + " maxInfo='" + maxInfo + '\''
            + '}';
    }
}
