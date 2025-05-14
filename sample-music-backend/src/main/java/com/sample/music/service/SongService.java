package com.sample.music.service;

import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.sample.music.constant.SortType;
import com.sample.music.constant.TargetType;
import com.sample.music.exception.BusinessException;
import com.sample.music.mapper.AlbumMapper;
import com.sample.music.mapper.ArtistMapper;
import com.sample.music.mapper.SongMapper;
import com.sample.music.pojo.dto.PageBean;
import com.sample.music.pojo.dto.SongUpload;
import com.sample.music.pojo.entity.Album;
import com.sample.music.pojo.entity.Artist;
import com.sample.music.pojo.entity.Song;
import com.sample.music.pojo.vo.view.SongView;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.sample.music.constant.HttpStatusCode.NOT_FOUND;
import static com.sample.music.constant.HttpStatusCode.INTERNAL_SERVER_ERROR;

@Service
@Transactional
@RequiredArgsConstructor
public class SongService {

    private final SongMapper songMapper;

    private final ArtistMapper artistMapper;

    private final AlbumMapper albumMapper;

    private final FileManageService fileManageService;

    private final RedissonClient redissonClient;

    private final static Gson GSON = new Gson();

    /**
     * 新增歌曲，解析上传（管理员）
     *
     */
    /*public Song insertAndParseSong(Song song) {
        song.setTags(GSON.toJson(song.getTags()));
        try {
            // 将 MultipartFile 转换为 File
            File file = songToFile(song.getAudioFiles());
            // 解析歌曲源信息
            AudioFile audio = AudioFileIO.read(file);
            Tag tag = audio.getTag();

            // 歌曲名
            String title = tag.getFirst(FieldKey.TITLE);
            // 艺术家
            String artist = tag.getFirst(FieldKey.ARTIST);
            // 专辑
            String album = tag.getFirst(FieldKey.ALBUM);

            // 封面
            byte[] coverArt = null;
            if (tag instanceof FlacTag) {
                Artwork artwork = tag.getFirstArtwork();
                if (artwork != null) {
                    coverArt = artwork.getBinaryData();
                }
            }
            // 歌词（如果存在）
            byte[] lyrics = tag.getFirst(FieldKey.LYRICS).getBytes();

            // 上传歌词文件到OSS
            String lyricUrl = fileManageService.uploadByte(lyrics, title, artist, ".lrc", "lyric");
            // 上传图片文件到OSS
            String coverUrl = fileManageService.uploadByte(coverArt, title, artist, ".jpg", "cover");
            // 上传歌曲文件到OSS
            String songUrl = fileManageService.uploadFile(song.getAudioFiles(), "song");

            if (!lyricUrl.equals("error") && !coverUrl.equals("error") && !songUrl.equals("error")) {
                // 将解析出的信息存入数据库
                song.setTitle(title);
                if (artistService.selectArtistByName(artist) == null) {
                    Artist a = new Artist();
                    a.setName(artist);
                    artistService.insertArtistByOne(a);
                }
                song.setArtist(artist);
                if (albumService.selectAlbumByTitle(album) == null) {
                    Album a = new Album();
                    a.setTitle(album);
                    a.setArtist(artist);
                    albumService.insertAlbumByOne(a);
                }
                song.setAlbum(album);
                song.setLyric(lyricUrl);
                song.setCover(coverUrl);
                song.setUrl(songUrl);
                // 保存其它信息到数据库
                songMapper.insertSong(song);

                return song;
            } else {
                throw new BusinessException(INTERNAL_SERVER_ERROR, "文件上传失败");
            }
        } catch (IOException | CannotReadException | TagException | InvalidAudioFrameException |
                 ReadOnlyFileException e) {
            throw new BusinessException(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // 转换文件类型
    private File songToFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new IOException("文件为空");
        }
        String originalFilename = multipartFile.getOriginalFilename();
        assert originalFilename != null;
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        File tempFile = File.createTempFile("upload", extension);
        try (InputStream inputStream = multipartFile.getInputStream();
             OutputStream outputStream = new FileOutputStream(tempFile)) {
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            tempFile.delete(); // 删除临时文件
            throw e;
        }
        return tempFile;
    }*/

    /**
     * 新增歌曲（管理员）
     *
     * @param songUpload 歌曲上传信息
     * @return Result<Song>
     */
    public boolean insertSong(SongUpload songUpload) {
        Song song = new Song();

        // 上传歌词文件到OSS
        if (songUpload.getLyric().isEmpty()) {
            song.setLyric(null);
        } else {
            String lyricUrl = fileManageService.uploadFile(songUpload.getLyric(), "lyric");
            if (!lyricUrl.equals("error")) song.setLyric(lyricUrl);
            else throw new BusinessException(INTERNAL_SERVER_ERROR, "歌词文件上传失败");
        }

        // 上传图片文件到OSS
        if (songUpload.getImageFiles().isEmpty()) {
            song.setCover(null);
        } else {
            String coverUrl = fileManageService.uploadFile(songUpload.getImageFiles(), "cover");
            if (!coverUrl.equals("error")) song.setCover(coverUrl);
            else throw new BusinessException(INTERNAL_SERVER_ERROR, "歌曲封面文件上传失败");
        }

        // 上传mp3歌曲文件到OSS
        if (songUpload.getMp3Url().isEmpty()) {
            song.setMp3Url(null);
        } else {
            String mp3SongUrl = fileManageService.uploadFile(songUpload.getMp3Url(), "song");
            if (!mp3SongUrl.equals("error")) song.setMp3Url(mp3SongUrl);
            else throw new BusinessException(INTERNAL_SERVER_ERROR, "mp3格式歌曲文件上传失败");
        }

        // 上传无损歌曲文件到OSS
        if (songUpload.getFlacUrl().isEmpty()) {
            song.setFlacUrl(null);
        } else {
            String flacSongUrl = fileManageService.uploadFile(songUpload.getFlacUrl(), "song");
            if (!flacSongUrl.equals("error")) song.setFlacUrl(flacSongUrl);
            else throw new BusinessException(INTERNAL_SERVER_ERROR, "flac格式歌曲文件上传失败");
        }

        try {
            song.setListeners(0);
            // 转换标签
            List<String> tags = songUpload.getTags();
            song.setTags(GSON.toJson(tags));
            // 转换歌手
            List<String> artists = songUpload.getArtists();
            song.setArtists(GSON.toJson(artists));
            songMapper.insertSong(song);
            return true;
        } catch (Exception e) {
            throw new BusinessException(INTERNAL_SERVER_ERROR, "上传歌曲失败");
        }
    }

    /**
     * 删除歌曲（管理员）
     *
     * @param songId 歌曲Id
     */
    public boolean deleteAndRemoveSong(Long songId) {
        SongView song = this.querySongById(songId);
        if (song == null) throw new BusinessException(NOT_FOUND, "未找到歌曲");
        if (song.getLyric() != null) fileManageService.delete(song.getLyric());
        if (song.getCover() != null) fileManageService.delete(song.getCover());
        if (song.getFlacUrl() != null) fileManageService.delete(song.getFlacUrl());
        if (song.getMp3Url() != null) fileManageService.delete(song.getMp3Url());
        songMapper.deleteSongById(songId);
        return true;
    }

    /**
     * 修改歌曲（管理员）
     *
     * @param song 歌曲
     */
    public Song updateSong(Song song) {
        if (querySongById(song.getId()) == null) throw new BusinessException(404, "未找到歌曲");
        songMapper.updateSong(song);
        return song;
    }

    /**
     * 条件并分页查询歌曲 todo
     *
     * @param condition 条件
     * @param params    条件字段
     * @param pageNum   分页参数，页码
     * @param pageSize  分页参数，每页大小
     * @param sortType  排序字段
     * @return PageBean<Song>
     */
    @Cacheable(
            value = "songPageCache",
            key = "{#condition, #params, #pageNum, #pageSize, #sortType?.name()}"
    )
    public PageBean<SongView> query(
            String condition,
            TargetType params,
            Integer pageNum,
            Integer pageSize,
            SortType sortType)
    {
        // 先获取总数
        int total;
        if (condition == null || condition.isEmpty()) {
            // 没有条件，执行总数查询
            total = songMapper.countAll();
        } else {
            // 有条件，执行条件总数查询
            total = songMapper.countByCondition(condition, params.getType());
        }
        PageBean<SongView> pb = new PageBean<>();
        // 设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        if (sortType != null) {
            PageHelper.orderBy(String.format("%s %s", sortType.getField(), sortType.getDirection()));
        }
        // 执行分页查询
        List<Song> songs;
        List<SongView> songViews = new ArrayList<>();
        if (condition == null || condition.isEmpty()) {
            // 没有条件，执行分页查询
            songs = songMapper.PagedQuery();
        } else {
            // 有条件，执行条件查询
            songs = songMapper.conditionAndPagedQuery(condition, params.getType());
        }
        songs.forEach(song -> {
            SongView songView = new SongView();
            BeanUtils.copyProperties(song, songView);
            songView.setTags(JSONUtil.toList(song.getTags(), String.class));
            songView.setArtists(JSONUtil.toList(song.getArtists(), String.class));
            if (song.getArtist() != null) {
                Artist artist = artistMapper.selectArtistByName(song.getArtist());
                if (artist != null) songView.setArtistAvatar(artist.getAvatar());
            }
            if (song.getAlbum() != null) {
                Album album = albumMapper.selectAlbumByTitle(song.getAlbum());
                if (album != null) songView.setAlbumCover(album.getCover());
            }
            songViews.add(songView);
        });
        pb.setTotal(total); // 直接使用分页结果的size作为总数
        pb.setItems(songViews);
        return pb;
    }

    @Cacheable(
            value = "AdminSelectSongCache",
            key = "{#condition, #params, #pageNum, #pageSize, #sortType?.name()}"
    )
    public PageBean<Song> select(
            String condition,
            TargetType params,
            Integer pageNum,
            Integer pageSize,
            SortType sortType)
    {
        // 先获取总数
        int total;
        if (condition == null || condition.isEmpty()) {
            // 没有条件，执行总数查询
            total = songMapper.countAll();
        } else {
            // 有条件，执行条件总数查询
            total = songMapper.countByCondition(condition, params.getType());
        }
        PageBean<Song> pb = new PageBean<>();
        // 设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        if (sortType != null) {
            PageHelper.orderBy(String.format("%s %s", sortType.getField(), sortType.getDirection()));
        }
        // 执行分页查询
        List<Song> songs;
        List<SongView> songViews = new ArrayList<>();
        if (condition == null || condition.isEmpty()) {
            // 没有条件，执行分页查询
            songs = songMapper.PagedQuery();
        } else {
            // 有条件，执行条件查询
            songs = songMapper.conditionAndPagedQuery(condition, params.getType());
        }
        pb.setTotal(total); // 直接使用分页结果的size作为总数
        pb.setItems(songs);
        return pb;
    }

    /**
     * 根据id查询歌曲
     *
     * @param songId 歌曲id
     * @return Song
     */
    @Cacheable(value = "songDetailCache", key = "#songId")
    public SongView querySongById(Long songId) {
        Song song = songMapper.querySongById(songId);
        SongView songView = new SongView();
        BeanUtils.copyProperties(song, songView);
        songView.setTags(JSONUtil.toList(song.getTags(), String.class));
        songView.setArtists(JSONUtil.toList(song.getArtists(), String.class));
        return songView;
    }

    /**
     * 收听数+1
     *
     * @param id 歌曲id
     */
    public void listenersPlusOne(Long id) {
        songMapper.listenersPlusOne(id);
    }

    /**
     * 个性化推荐歌曲
     * todo
     */
    @Scheduled(cron = "0 0 3 * * ?") // 每天凌晨3点执行
    public void generateDailyRecommendations() {
        RLock lock = redissonClient.getLock("task_lock:daily_recommend");
        try {
            if (lock.tryLock()) { // 非阻塞尝试
                // 执行推荐算法计算...
            }
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
