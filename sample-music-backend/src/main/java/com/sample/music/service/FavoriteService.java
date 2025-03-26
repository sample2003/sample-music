package com.sample.music.service;

import com.sample.music.exception.BusinessException;
import com.sample.music.mapper.*;
import com.sample.music.pojo.entity.Favorites;
import com.sample.music.pojo.vo.favorite.*;
import com.sample.music.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.sample.music.constant.HttpStatusCode.CONFLICT;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteMapper favoriteMapper;

    private final SongMapper songMapper;

    private final PlaylistMapper playlistMapper;

    private final AlbumMapper albumMapper;

    private final UserMapper userMapper;

    private final PublicityMapper publicityMapper;

    private final CommentMapper commentMapper;

    /**
     * 添加到收藏
     *
     * @param favorites 收藏信息
     */
    public void AddToFavorites(Favorites favorites) {
        try {
            // 尝试插入收藏记录
            favoriteMapper.AddToFavorites(favorites);

            String type = favorites.getTargetType();
            Long id = favorites.getTargetId();

            if(Objects.equals(type, "album")) {
                albumMapper.favoritesPlus(id);
            }else if(Objects.equals(type, "playlist")) {
                playlistMapper.favoritesPlus(id);
            }else if(Objects.equals(type, "comment")) {
                commentMapper.favoritesPlus(id);
            }
        } catch (DataAccessException ex) {
            if (ex.getCause() instanceof SQLIntegrityConstraintViolationException) {
                // 捕获唯一性约束违反异常
                // 返回一个自定义的错误消息给客户端
                throw new BusinessException(CONFLICT, "您已经收藏过这个内容");
            } else {
                // 其他数据库异常处理
                throw ex;
            }
        }
    }

    /**
     * 移除收藏
     *
     * @param targetType 收藏类型
     * @param targetId 收藏id
     */
    public void RemoveByFavorites(String targetType, Long targetId) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer id = (Integer) claims.get("id");
        Long userId = Long.valueOf(id);
        if(Objects.equals(targetType, "album")) {
            albumMapper.favoriteSubtract(targetId);
        }else if(Objects.equals(targetType, "playlist")) {
            playlistMapper.favoriteSubtract(targetId);
        }else if(Objects.equals(targetType, "comment")) {
            commentMapper.favoriteSubtract(targetId);
        }
        favoriteMapper.RemoveByFavorites(userId, targetType, targetId);
    }

    /**
     * 查看我的收藏歌曲
     *
     */
    public List<FavoriteSong> queryFavoritesSong() {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer id = (Integer) claims.get("id");
        Long userId = Long.valueOf(id);
        String targetType = "song";
        return favoriteMapper.queryFavoritesSong(targetType, userId);
    }

    /**
     * 查看我的收藏歌单
     *
     */
    public List<FavoritePlaylist> queryFavoritePlaylist() {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer id = (Integer) claims.get("id");
        Long userId = Long.valueOf(id);
        String targetType = "playlist";
        return favoriteMapper.queryFavoritePlaylist(targetType, userId);
    }

    /**
     * 查看我的收藏专辑
     *
     */
    public List<FavoriteAlbum> queryFavoriteAlbum() {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer id = (Integer) claims.get("id");
        Long userId = Long.valueOf(id);
        String targetType = "album";
        return favoriteMapper.queryFavoriteAlbum(targetType, userId);
    }

    /**
     * 查看我关注的用户
     *
     */
    public List<FavoriteUser> queryFavoriteUser() {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer id = (Integer) claims.get("id");
        Long userId = Long.valueOf(id);
        String targetType = "user";
        return favoriteMapper.queryFavoriteUser(targetType, userId);
    }

    /**
     * 查看我点赞的评论
     *
     */
    public List<FavoriteComment> queryFavoriteComment() {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer id = (Integer) claims.get("id");
        Long userId = Long.valueOf(id);
        String targetType = "comment";
        return favoriteMapper.queryFavoriteComment(targetType, userId);
    }
}
