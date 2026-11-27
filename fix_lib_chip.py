import os

config_path = "core/common/src/commonMain/kotlin/com/maxrave/common/Config.kt"
library_vm_path = "composeApp/src/commonMain/kotlin/com/maxrave/simpmusic/viewModel/LibraryViewModel.kt"

# Fix Config.kt
with open(config_path, "r") as f:
    lines = f.readlines()
with open(config_path, "w") as f:
    for line in lines:
        if '"youtube_music_playlist" -> YOUR_LIBRARY' in line:
            f.write(line.replace('"youtube_music_playlist" -> YOUR_LIBRARY', '"youtube_music_playlist" -> YOUTUBE_MUSIC_PLAYLIST'))
        else:
            f.write(line)

# Fix LibraryViewModel.kt
with open(library_vm_path, "r") as f:
    lines = f.readlines()
with open(library_vm_path, "w") as f:
    for line in lines:
        if "MutableStateFlow(LibraryChipType.YOUR_LIBRARY)" in line:
            f.write(line.replace("MutableStateFlow(LibraryChipType.YOUR_LIBRARY)", "MutableStateFlow(LibraryChipType.YOUTUBE_MUSIC_PLAYLIST)"))
        else:
            f.write(line)
