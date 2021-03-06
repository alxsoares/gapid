# Copyright (C) 2017 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

set(api gles/gles.api)
apic(${api} TEMPLATE api.go.tmpl OUTPUTS api.go enum.go)
apic(${api} TEMPLATE api.proto.tmpl OUTPUTS gles_pb/api.proto
    DEFINES GoPackage=github.com/google/gapid/gapis/gfxapi/gles/gles_pb)
apic(${api} TEMPLATE mutate.go.tmpl OUTPUTS mutate.go)
apic(${api} TEMPLATE convert.go.tmpl OUTPUTS convert.go)

annotate(${api})
embed(
    OUTPUT "snippets_embed.go"
    snippets.base64
    globals_snippets.base64
)
