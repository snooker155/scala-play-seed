package v1.post

import javax.inject.{Inject, Singleton}
import akka.actor.ActorSystem
import play.api.libs.concurrent.CustomExecutionContext
import play.api.{Logger, MarkerContext}
import scala.concurrent.Future

final case class PostData(id: PostId, title: String, body: String)

class PostId private (val underlying: Int) extends AnyVal {
  override def toString: String = underlying.toString
}

object PostId {
  def apply(raw: String): PostId = {
    require(raw != null)
    new PostId(Integer.parseInt(raw))
  }
}

class PostExecutionContext @Inject()(actorSystem: ActorSystem) extends CustomExecutionContext(actorSystem, "repository.dispatcher")

/**
  * A pure non-blocking interface for the PostRepository.
  */
trait PostRepository {
  def create(data: PostData)(implicit mc: MarkerContext): Future[PostId]

  def list()(implicit mc: MarkerContext): Future[Iterable[PostData]]

  def get(id: PostId)(implicit mc: MarkerContext): Future[Option[PostData]]
}

/**
  * A trivial implementation for the Post Repository.
  *
  * A custom execution context is used here to establish that blocking operations should be
  * executed in a different thread than Play's ExecutionContext, which is used for CPU bound tasks
  * such as rendering.
  */
@Singleton
class PostRepositoryImpl @Inject()()(implicit ec: PostExecutionContext) extends PostRepository {

  private val logger = Logger(this.getClass)

  private val postList = List(
    PostData(PostId("1"), "title 1", "blog post 1"),
    PostData(PostId("2"), "title 2", "blog post 2"),
    PostData(PostId("3"), "title 3", "blog post 3"),
    PostData(PostId("4"), "title 4", "blog post 4"),
    PostData(PostId("5"), "title 5", "blog post 5"),
    PostData(PostId("6"), "title 6", "blog post 6"),
    PostData(PostId("7"), "title 7", "blog post 7"),
    PostData(PostId("8"), "title 8", "blog post 8"),
    PostData(PostId("9"), "title 9", "blog post 9"),
    PostData(PostId("10"), "title 10", "blog post 10"),
    PostData(PostId("11"), "title 11", "blog post 11"),
    PostData(PostId("12"), "title 12", "blog post 12"),
    PostData(PostId("13"), "title 13", "blog post 13"),
    PostData(PostId("14"), "title 14", "blog post 14"),
    PostData(PostId("15"), "title 15", "blog post 15"),
    PostData(PostId("16"), "title 16", "blog post 16"),
    PostData(PostId("17"), "title 17", "blog post 17"),
    PostData(PostId("18"), "title 18", "blog post 18"),
    PostData(PostId("19"), "title 19", "blog post 19"),
    PostData(PostId("20"), "title 20", "blog post 20")
  )

  override def list()(implicit mc: MarkerContext): Future[Iterable[PostData]] = {
    Future {
      logger.trace(s"list: ")
      postList
    }
  }

  override def get(id: PostId)(implicit mc: MarkerContext): Future[Option[PostData]] = {
    Future {
      logger.trace(s"get: id = $id")
      postList.find(post => post.id == id)
    }
  }

  def create(data: PostData)(implicit mc: MarkerContext): Future[PostId] = {
    Future {
      logger.trace(s"create: data = $data")
      data.id
    }
  }

}