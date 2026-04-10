package com.chuwa.redbook.service.impl;

import com.chuwa.redbook.dao.CommentRepository;
import com.chuwa.redbook.dao.PostRepository;
import com.chuwa.redbook.entity.Comment;
import com.chuwa.redbook.entity.Post;
import com.chuwa.redbook.exception.BlogAPIException;
import com.chuwa.redbook.exception.ResourceNotFoundException;
import com.chuwa.redbook.payload.CommentDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock(name = "modelMapper")
    private ModelMapper modelMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Post post;
    private Post anotherPost;
    private Comment comment;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        post = new Post();
        post.setId(1L);

        anotherPost = new Post();
        anotherPost.setId(2L);

        comment = new Comment();
        comment.setId(10L);
        comment.setName("Tom");
        comment.setEmail("tom@chuwa.com");
        comment.setBody("hello comment body");
        comment.setPost(post);

        commentDto = new CommentDto();
        commentDto.setId(10L);
        commentDto.setName("Tom");
        commentDto.setEmail("tom@chuwa.com");
        commentDto.setBody("hello comment body");
    }

    @Test
    void testCreateComment() {
        Comment mappedEntity = new Comment();
        mappedEntity.setName(commentDto.getName());
        mappedEntity.setEmail(commentDto.getEmail());
        mappedEntity.setBody(commentDto.getBody());

        Mockito.when(modelMapper.map(ArgumentMatchers.any(CommentDto.class), ArgumentMatchers.eq(Comment.class)))
                .thenReturn(mappedEntity);
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.save(ArgumentMatchers.any(Comment.class))).thenReturn(comment);
        Mockito.when(modelMapper.map(ArgumentMatchers.any(Comment.class), ArgumentMatchers.eq(CommentDto.class)))
                .thenReturn(commentDto);

        CommentDto result = commentService.createComment(1L, commentDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(commentDto.getId(), result.getId());
        Assertions.assertEquals(commentDto.getName(), result.getName());
        Assertions.assertEquals(commentDto.getEmail(), result.getEmail());
        Assertions.assertEquals(commentDto.getBody(), result.getBody());

        ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);
        Mockito.verify(commentRepository).save(captor.capture());
        Assertions.assertEquals(post.getId(), captor.getValue().getPost().getId());
    }

    @Test
    void testCreateComment_PostNotFound() {
        Mockito.when(modelMapper.map(ArgumentMatchers.any(CommentDto.class), ArgumentMatchers.eq(Comment.class)))
                .thenReturn(new Comment());
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> commentService.createComment(1L, commentDto));
        Mockito.verify(commentRepository, Mockito.never()).save(ArgumentMatchers.any(Comment.class));
    }

    @Test
    void testGetCommentsByPostId() {
        Comment comment2 = new Comment();
        comment2.setId(11L);
        comment2.setName("Jerry");
        comment2.setEmail("jerry@chuwa.com");
        comment2.setBody("another comment body");
        comment2.setPost(post);

        CommentDto dto2 = new CommentDto(11L, "Jerry", "jerry@chuwa.com", "another comment body");

        Mockito.when(commentRepository.findByPostId(1L)).thenReturn(Arrays.asList(comment, comment2));
        Mockito.when(modelMapper.map(ArgumentMatchers.any(Comment.class), ArgumentMatchers.eq(CommentDto.class)))
                .thenReturn(commentDto, dto2);

        Assertions.assertEquals(2, commentService.getCommentsByPostId(1L).size());
    }

    @Test
    void testGetCommentsByPostId_EmptyList() {
        Mockito.when(commentRepository.findByPostId(1L)).thenReturn(Collections.emptyList());

        Assertions.assertTrue(commentService.getCommentsByPostId(1L).isEmpty());
        Mockito.verify(modelMapper, Mockito.never())
                .map(ArgumentMatchers.any(Comment.class), ArgumentMatchers.eq(CommentDto.class));
    }

    @Test
    void testGetCommentById() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.findById(10L)).thenReturn(Optional.of(comment));
        Mockito.when(modelMapper.map(comment, CommentDto.class)).thenReturn(commentDto);

        CommentDto result = commentService.getCommentById(1L, 10L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(commentDto.getId(), result.getId());
    }

    @Test
    void testGetCommentById_PostNotFound() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> commentService.getCommentById(1L, 10L));
        Mockito.verify(commentRepository, Mockito.never()).findById(ArgumentMatchers.anyLong());
    }

    @Test
    void testGetCommentById_CommentNotFound() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.findById(10L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> commentService.getCommentById(1L, 10L));
    }

    @Test
    void testGetCommentById_CommentDoesNotBelongToPost() {
        comment.setPost(anotherPost);
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.findById(10L)).thenReturn(Optional.of(comment));

        BlogAPIException ex = Assertions.assertThrows(BlogAPIException.class,
                () -> commentService.getCommentById(1L, 10L));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
        Assertions.assertEquals("Comment does not belong to post", ex.getMessage());
    }

    @Test
    void testUpdateComment() {
        CommentDto request = new CommentDto();
        request.setName("UpdatedName");
        request.setEmail("updated@chuwa.com");
        request.setBody("updated comment body");

        CommentDto updatedDto = new CommentDto(10L, request.getName(), request.getEmail(), request.getBody());

        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.findById(10L)).thenReturn(Optional.of(comment));
        Mockito.when(commentRepository.save(ArgumentMatchers.any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(modelMapper.map(ArgumentMatchers.any(Comment.class), ArgumentMatchers.eq(CommentDto.class)))
                .thenReturn(updatedDto);

        CommentDto result = commentService.updateComment(1L, 10L, request);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("UpdatedName", result.getName());
        Assertions.assertEquals("updated@chuwa.com", result.getEmail());
        Assertions.assertEquals("updated comment body", result.getBody());

        ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);
        Mockito.verify(commentRepository).save(captor.capture());
        Assertions.assertEquals(request.getName(), captor.getValue().getName());
        Assertions.assertEquals(request.getEmail(), captor.getValue().getEmail());
        Assertions.assertEquals(request.getBody(), captor.getValue().getBody());
    }

    @Test
    void testUpdateComment_PostNotFound() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> commentService.updateComment(1L, 10L, commentDto));
        Mockito.verify(commentRepository, Mockito.never()).findById(ArgumentMatchers.anyLong());
    }

    @Test
    void testUpdateComment_CommentNotFound() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.findById(10L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> commentService.updateComment(1L, 10L, commentDto));
    }

    @Test
    void testUpdateComment_CommentDoesNotBelongToPost() {
        comment.setPost(anotherPost);
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.findById(10L)).thenReturn(Optional.of(comment));

        BlogAPIException ex = Assertions.assertThrows(BlogAPIException.class,
                () -> commentService.updateComment(1L, 10L, commentDto));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
        Assertions.assertEquals("Comment does not belong to post", ex.getMessage());
        Mockito.verify(commentRepository, Mockito.never()).save(ArgumentMatchers.any(Comment.class));
    }

    @Test
    void testDeleteComment() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.findById(10L)).thenReturn(Optional.of(comment));
        Mockito.doNothing().when(commentRepository).delete(comment);

        commentService.deleteComment(1L, 10L);

        Mockito.verify(commentRepository, Mockito.times(1)).delete(comment);
    }

    @Test
    void testDeleteComment_PostNotFound() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> commentService.deleteComment(1L, 10L));
        Mockito.verify(commentRepository, Mockito.never()).findById(ArgumentMatchers.anyLong());
        Mockito.verify(commentRepository, Mockito.never()).delete(ArgumentMatchers.any(Comment.class));
    }

    @Test
    void testDeleteComment_CommentNotFound() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.findById(10L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> commentService.deleteComment(1L, 10L));
        Mockito.verify(commentRepository, Mockito.never()).delete(ArgumentMatchers.any(Comment.class));
    }

    @Test
    void testDeleteComment_CommentDoesNotBelongToPost() {
        comment.setPost(anotherPost);
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.findById(10L)).thenReturn(Optional.of(comment));

        BlogAPIException ex = Assertions.assertThrows(BlogAPIException.class,
                () -> commentService.deleteComment(1L, 10L));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
        Assertions.assertEquals("Comment does not belong to post", ex.getMessage());
        Mockito.verify(commentRepository, Mockito.never()).delete(ArgumentMatchers.any(Comment.class));
    }

    @Test
    void testCommentServiceMapperUtil() {
        CommentDto result = CommentServiceImpl.commentServiceMapperUtil(comment);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(comment.getId(), result.getId());
        Assertions.assertEquals(comment.getName(), result.getName());
        Assertions.assertEquals(comment.getEmail(), result.getEmail());
        Assertions.assertEquals(comment.getBody(), result.getBody());
    }
}
