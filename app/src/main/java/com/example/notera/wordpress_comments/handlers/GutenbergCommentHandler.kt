package com.example.notera.wordpress_comments

import org.wordpress.aztec.handlers.GenericBlockHandler

class GutenbergCommentHandler : GenericBlockHandler<GutenbergCommentSpan>(GutenbergCommentSpan::class.java)