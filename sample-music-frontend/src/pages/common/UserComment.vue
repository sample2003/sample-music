<template>
  <div id="userComment">
    <TextArea @submit="insertContent"></TextArea>
    <div v-if="user_comment && user_comment.length > 0" class="comments">
      <div
          class="comment"
          v-for="c in user_comment"
          :key="c.id"
          :style="{border: userDetail.username === c.username ? '1px solid var(--main-color)' : '1px solid #0000'}"
      >
        <div class="user-detail flex">
          <img :src="Icon.peopleIcon" alt="">
          <span :style="{color: userDetail.username === c.username ? 'var(--main-color)' : '#888888'}">{{
              c.username
            }}</span>
        </div>
        <div class="uc">
          <span style="text-align: start">{{ c.content }}</span>
        </div>
        <div class="comment-detail flex">
          <span style="color: #888888;font-size: 0.9em; white-space: nowrap;">{{ c.createTime }}</span>
          <img :src="isGoodArray.includes(c.id) ? Icon.likeIcon : Icon.notLikeIcon" alt=""
               @click="changeFavoritesComment(c.id)">
          <span>{{ c.likes }}</span>
        </div>
      </div>
    </div>
    <div v-else style="padding: 10px"><h3>暂无评论</h3></div>
  </div>
</template>

<script>
import TextArea from "@/pages/common/TextArea.vue";
import AlbumService from "@/api/service/AlbumService";
import FavoriteService from "@/api/service/FavoriteService";
import PlaylistService from "@/api/service/PlaylistService";
import SongService from "@/api/service/SongService";
import Icon from "@/util/common/Icon";

export default {
  name: "UserComment",
  computed: {
    Icon() {
      return Icon
    }
  },
  components: {TextArea},
  props: ['detailType', 'targetId'],
  data() {
    return {
      user_comment: [],
      isGoodArray: [], // 存储每个评论的点赞状态,
      key: 0,
    }
  },
  watch: {},
  methods: {
    // 评论
    async insertContent(comment) {
      if (this.detailType === 'album') {
        try {
          await AlbumService.insertAlbumComment(comment, this.targetId);
          await this.queryComment();
        } catch (error) {
          console.error('插入专辑评论出错：', error);
        }
      } else if (this.detailType === 'playlist') {
        try {
          await PlaylistService.insertPlaylistComment(comment, this.targetId);
          await this.queryComment();
        } catch (error) {
          console.error('插入播放列表评论出错：', error);
        }
      } else if (this.detailType === 'song') {
        try {
          await SongService.insertSongComment(comment, this.targetId);
          await this.queryComment();
        } catch (error) {
          console.error('插入歌曲评论出错：', error);
        }
      }
    },
    // 点赞或取消点赞评论
    async changeFavoritesComment(id) {
      const isGood = await this.isGoodComment(id);
      if (isGood) {
        // 取消点赞时，先从isGoodArray中移除对应的id
        const index = this.isGoodArray.indexOf(id);
        if (index > -1) {
          this.isGoodArray.splice(index, 1);
        }
        await FavoriteService.removeByFavorites("comment", id);
        await this.queryComment();
      } else {
        await FavoriteService.addToFavorites("comment", id);
        await this.queryComment();
      }
    },
    // 查询是否点赞评论
    async isGoodComment(id) {
      const favoriteComment = await FavoriteService.queryFavoriteComment();
      if (favoriteComment) return favoriteComment.some(favorite => favorite.id === id);
    },
    // 查询评论
    async queryComment() {
      let comment;
      if (this.detailType === 'album') {
        comment = await AlbumService.queryCommentPaged(this.targetId, 1, 100);
      } else if (this.detailType === 'playlist') {
        comment = await PlaylistService.queryCommentPaged(this.targetId, 1, 100);
      } else if (this.detailType === 'song') {
        comment = await SongService.queryCommentPaged(this.targetId, 1, 100);
      }
      if(comment && comment.items) {
        this.user_comment = comment.items;
        for (const c of comment.items) {
          const isGood = await this.isGoodComment(c.id);
          if (isGood) {
            this.$set(this.isGoodArray, this.isGoodArray.length, c.id);
          }
        }
      }
    },
  },
  mounted() {
    this.$on("qc", this.queryComment);
    this.queryComment();
  },
  beforeDestroy() {
    this.$off("qc");
  }
}
</script>

<style scoped>
#userComment {
  width: 100%;
  height: 100%;
  overflow: auto;
}

.comments {
  width: 90%;
  margin-left: 5%;
  line-height: 20px;
  column-gap: 20px; /* 列与列之间的间隙 */
}

/* 默认情况下，屏幕宽度较大时显示4列 */
@media (min-width: 1200px) {
  .comments {
    column-count: 4;
  }
}

/* 当屏幕宽度在992px到1199px之间时显示3列 */
@media (min-width: 992px) and (max-width: 1199px) {
  .comments {
    column-count: 3;
  }
}

/* 当屏幕宽度在768px到991px之间时显示2列 */
@media (min-width: 768px) and (max-width: 991px) {
  .comments {
    column-count: 2;
  }
}

/* 当屏幕宽度小于768px时显示1列 */
@media (max-width: 767px) {
  .comments {
    column-count: 1;
  }
}

.comment {
  break-inside: avoid; /* 防止评论被分割到两列中 */
  margin-bottom: 10px; /* 评论之间的垂直间隙 */
  padding: 1%;
  border-radius: 5px;
  box-shadow: 2px 2px 4px #b3b3b3, -2px -2px 4px #ffffff;
}

.user-detail {
  width: 100%;
  gap: 10px;
  justify-content: start;
}

.user-detail img {
  width: 30px;
  height: 30px;
  border-radius: 50%;
}

.uc {
  padding: 10px;
  display: flex;
  justify-content: start;
}

.comment-detail {
  width: 100%;
  cursor: default;
  justify-content: start;
}

.comment-detail img {
  width: 20px;
  height: 20px;
  margin-left: 10px;
  cursor: pointer;
}
</style>