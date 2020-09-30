package com.kaspar.blongular.blongular.service;


import com.kaspar.blongular.blongular.dto.PostDto;
import com.kaspar.blongular.blongular.exception.PostNotFoundException;
import com.kaspar.blongular.blongular.model.Post;
import com.kaspar.blongular.blongular.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class PostService {

    @Autowired
    private AuthService authService;

    @Autowired
    private PostRepository postRepository;

    public List<PostDto> showAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(this::mapFromPostToDto).collect(toList());
    }

    private PostDto mapFromPostToDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setUsername(post.getUsername());
        return postDto;
    }

    private PostDto mapFromDtoToPost(PostDto postDto) {
        Post post = new Post();
        post.setId(postDto.getId());
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setUsername(postDto.getUsername());
        return postDto;
    }

    public PostDto readSinglePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("For id " + id));
        return mapFromPostToDto(post);
    }

    public void createPost(PostDto postDto) {


        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());

        User username = authService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("No user logged in!"));
        post.setUsername(username.getUsername());

        post.setCreatedOn(Instant.now());

        postRepository.save(post);

    }
}
