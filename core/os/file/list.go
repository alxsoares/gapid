// Copyright (C) 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package file

import "path/filepath"

// PathList is a list of file paths.
type PathList []Path

// Paths returns a PathList formed from the list of paths.
func Paths(files ...Path) PathList {
	return PathList(files)
}

// SplitList splits a string on the system path list separator, and then resolves each produced string to a Path,
// returning the paths produced as a PathList.
func SplitList(paths string) PathList {
	result := PathList{}
	for _, name := range filepath.SplitList(paths) {
		result = append(result, Abs(name))
	}
	return result
}

// AsSet returns the contents of the PathList as a PathSet.
func (l PathList) AsSet() PathSet {
	return PathSet{}.Append(l...)
}

// Append is analogous to the append function.
func (l PathList) Append(paths ...Path) PathList {
	return append(l, paths...)
}

// Contains tests to see if the list contains the path.
func (l PathList) Contains(path Path) bool {
	for _, old := range l {
		if path == old {
			return true
		}
	}
	return false
}

// RootOf returns the first Path that contains the path, or an empty path if not found.
func (l PathList) RootOf(p Path) Path {
	for _, entry := range l {
		if entry.Contains(p) {
			return entry
		}
	}
	return Path{}
}

// Matching returns the list of files that matches any supplied pattern.
func (l PathList) Matching(patterns ...string) PathList {
	out := make(PathList, 0, len(l))
	for _, path := range l {
		if path.Matches(patterns...) {
			out = append(out, path)
		}
	}
	return out
}

// NotMatching returns the list of files that does not match any supplied pattern.
func (l PathList) NotMatching(patterns ...string) PathList {
	out := make(PathList, 0, len(l))
	for _, path := range l {
		if !path.Matches(patterns...) {
			out = append(out, path)
		}
	}
	return out
}

// Len is the number of elements in the collection,
// so PathList implements sort.Interface.
func (l PathList) Len() int { return len(l) }

// Less reports whether the element with index i should sort before the element with index j,
// so PathList implements sort.Interface.
func (l PathList) Less(i, j int) bool { return l[i].value < l[j].value }

// Swap swaps the elements with indexes i and j,
// so PathList implements sort.Interface.
func (l PathList) Swap(i, j int) { l[i], l[j] = l[j], l[i] }
