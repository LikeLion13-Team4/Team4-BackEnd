package com.project.team4backend.domain.post.converter;

import com.project.team4backend.domain.member.entity.Member;
import com.project.team4backend.domain.post.dto.request.PostReqDTO;
import com.project.team4backend.domain.post.entity.Post;
import com.project.team4backend.domain.post.entity.PostImage;

public class PostConverter {
    public static Post toEntity(PostReqDTO.PostCreateReqDTO req, Member member) {
        Post post = Post.builder()
                .title(req.title())
                .content(req.content())
                .tags(req.tags())
                .member(member)
                .build();

        if (req.imageUrls() != null) {
            req.imageUrls().forEach(url ->
                    post.getImages().add(PostImage.builder()
                            .post(post)
                            .imageUrl(url)
                            .build())
            );
        }//쓰읍;;;

        return post;
    }

}
