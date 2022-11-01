package project.vinyl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import project.vinyl.dto.PostFormDto;
import project.vinyl.dto.PostViewDto;
import project.vinyl.entity.Member;
import project.vinyl.entity.Post;
import project.vinyl.repository.PostRepository;

import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Post register(PostFormDto postFormDto, Member member){
        Post post = postFormDto.toEntity(member);
        return postRepository.save(post);
    }

    public List<PostViewDto> getList() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id", "regTime");
        List<Post> list= postRepository.findAll(sort);
        return list.stream().map(PostViewDto::new).collect(Collectors.toList());
    }

    public Post getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new EntityExistsException("존재하지 않는 게시글입니다."));
        return post;
    }

    public void hitsInc(Post post) {
        post.hitCnt();
    }

    public Post modify(Long postId, PostFormDto postFormDto) {

        Post post = postRepository.findById(postId).orElseThrow(() ->
                new EntityExistsException("존재하지 않는 게시글입니다."));
        post.update(postFormDto.getTitle(), postFormDto.getContent());
        return post;

    }

    public void delete(Long postId){
        Post post = getPost(postId);
        postRepository.delete(post);
    }
}
